package com.reign.server.core.alive;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ji on 15-12-4.
 */
public class AliveCheckZk {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliveCheckZk.class);

    private static AliveCheckZk zkUtil;
    private static String nameSpace;
    private static String nodesPath;
    private static String nodeId;
    private static String zkConnectionString;
    private static CuratorFramework client;
    private static LeaderLatch latch;

    private AliveCheckZk(String paramConnectionString, String paramNameSpace, String paramNodesPath, String paramNodeId) throws Exception {
        zkConnectionString = paramConnectionString;
        nameSpace = paramNameSpace;
        nodesPath = paramNodesPath;
        nodeId = paramNodeId;
        nodesPath = ZKPaths.makePath(nameSpace, nodesPath);
        //创建目录
        this.createClient();
        //添加节点变化监听
        this.addChildrenListener();
        //添加session超时监听
        this.addSessionTimeOutListener();
        //注册节点
        this.registNode();
        //开启leader选举
        this.startLeaderElect();
    }

    /**
     * 对外提供的注册节点的方法，节点启动时使用。会执行：
     * 1.创建永久目录
     *
     * @param zkConnectionString zk地址:端口
     * @param nameSpace          工程在zk中的nameSpace
     * @param nodesPath          节点父目录
     * @param nodeId             节点编号
     * @return
     * @throws Exception
     * @see #createClient()
     * 2.添加节点变化监听器
     * @see #addChildrenListener()
     * 3.添加session超时监听及处理
     * @see #addSessionTimeOutListener()
     * 4.注册节点
     * @see #registNode()
     * 5.开启Leader选举
     * @see #startLeaderElect()
     */
    public static synchronized AliveCheckZk regist(String zkConnectionString, String nameSpace, String nodesPath, String nodeId) throws Exception {
        if (zkUtil == null) {
            zkUtil = new AliveCheckZk(zkConnectionString, nameSpace, nodesPath, nodeId);
        }
        return zkUtil;
    }

    /**
     * close
     */
    public static void close() {
        try {
            if (client != null) {
                client.close();
            }
            if (latch != null) {
                latch.close();
            }
        } catch (Exception e) {
            LOGGER.error("Closing Zookeeper connection exception. ", e);
        }
    }

    /**
     * start Leader Election
     *
     * @throws Exception
     */
    private void startLeaderElect() throws Exception {
        latch = new LeaderLatch(client, nodesPath, nodeId);
        latch.addListener(new LeaderLatchListener() {
            public void isLeader() {
                //todo 节点选举为leader后，触发该逻辑
                LOGGER.warn("I am leader! HA ");
            }

            public void notLeader() {
                //todo 节点从leader改变为非leader后触发该逻辑
                LOGGER.warn("I am not leader");
            }
        });

        latch.start();
    }

    /**
     * register node to zookeeper when startup
     *
     * @throws Exception
     */
    private void registNode() throws Exception {
        String path = ZKPaths.makePath(nodesPath, nodeId);
        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            LOGGER.warn("Begin create zookeeper Node, nodePath:" + path);
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
            LOGGER.warn("Create zookeeper node success,nodePath:" + path);
        }
    }

    /**
     * add session timeout listener.
     * this method will be triggered when session timeout
     */
    private void addSessionTimeOutListener() {
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState == ConnectionState.LOST) {
                    LOGGER.error("Connection lost, try to reconnect!");
                    while (true) {
                        try {
                            if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                                String tmpNodePath = ZKPaths.makePath(nodesPath, nodeId);
                                client.create()
                                        .creatingParentsIfNeeded()
                                        .withMode(CreateMode.EPHEMERAL)
                                        .forPath(tmpNodePath);
                                LOGGER.info("Reconnect to Zookeeper success!");
                                break;
                            }
                        } catch (InterruptedException ie) {
                            break;
                        } catch (Exception e) {
                            LOGGER.error("Reconnect to Zookeeper Exception", e);
                        }
                    }
                }
            }
        });
    }

    /**
     * add children listener
     * this method will be triggered when online nodes change
     *
     * @throws Exception
     */
    private void addChildrenListener() throws Exception {
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, nodesPath, true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                LOGGER.warn("Online Nodes changed！！");
                List<String> children = client.getChildren().forPath(nodesPath);
                LOGGER.warn("Current online nodes: " + children);
            }
        });
        pathChildrenCache.start();
    }

    /**
     * Create client directory.
     * Online NameNode will be registered in this directory
     *
     * @throws Exception
     */
    private void createClient() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(10, 3);
        client = CuratorFrameworkFactory.builder()
                .namespace(nameSpace)
                .connectString(zkConnectionString)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(10 * 1000)//节点session超时时长
                .build();
        client.start();

        client.newNamespaceAwareEnsurePath(nodesPath);

        Stat stat = client.checkExists().forPath(nodesPath);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(nodesPath);
            LOGGER.warn("Created zookeeper Path :" + nodesPath);
        }
    }

}

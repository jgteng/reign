package com.reign.server.cache;

import com.google.common.cache.*;
import com.reign.component.constants.CoreConstant;
import com.reign.domain.task.Task;
import com.reign.server.dao.TaskNodeDao;
import com.reign.server.domain.CacheTaskNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 15-12-14.
 */
public class NodeCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCache.class);

    private static final TaskNodeDao taskNodeDao = new TaskNodeDao();

    private static final long NODE_TIME_OUT = 60 * 1000;

    private static final ConcurrentHashMap<Long, CacheTaskNodeInfo> _NODE_CACHE = new ConcurrentHashMap<Long, CacheTaskNodeInfo>();

    //key:taskNodeId,value:taskIds
    private static final ConcurrentHashMap<Long, Set<Long>> _NODE_TASK_CACHE = new ConcurrentHashMap<Long, Set<Long>>();

    private static final NodeCache _INSTANCE = new NodeCache();

    private NodeCache() {
    }

    public static NodeCache getInstance() {
        return _INSTANCE;
    }

    /**
     * get nodeInfo from cache.
     * Get from db while node info in cache is timeout
     *
     * @param nodeId
     * @return
     */
    public CacheTaskNodeInfo getNode(Long nodeId) {
        CacheTaskNodeInfo result = null;
        try {
            CacheTaskNodeInfo nodeInfo = _NODE_CACHE.get(nodeId);
            if (nodeInfo == null || nodeInfo.getCacheTime() == null || ((System.currentTimeMillis() - nodeInfo.getCacheTime().intValue()) > NODE_TIME_OUT)) {
                nodeInfo = taskNodeDao.getTaskNodeById(nodeId);
                if (nodeInfo != null) {
                    nodeInfo.setCacheTime(System.currentTimeMillis());
                    _NODE_CACHE.put(nodeInfo.getId(), nodeInfo);
                }
            }
            result = nodeInfo;
        } catch (Exception e) {
            LOGGER.error("Get taskNodeInfo from NodeCache error,[ taskNodeId:{} ]", nodeId, e);
        }
        return result;
    }

    /**
     * Add task to node running map
     *
     * @param nodeId
     * @param taskId
     */
    public void addTask(Long nodeId, Long taskId) {
        synchronized (_NODE_TASK_CACHE) {
            Set<Long> tasks = _NODE_TASK_CACHE.get(nodeId);
            if (tasks == null) {
                tasks = new HashSet<Long>();
                _NODE_TASK_CACHE.put(nodeId, tasks);
            }
            tasks.add(taskId);
        }
    }

    /**
     * remove task from node running map
     *
     * @param nodeId
     * @param taskId
     */
    public void removeTask(Long nodeId, Long taskId) {
        synchronized (_NODE_TASK_CACHE) {
            Set<Long> tasks = _NODE_TASK_CACHE.get(nodeId);
            if (tasks != null) {
                tasks.remove(taskId);
            }
        }
        try {
            RunningTasksFromTaskNodeCache.getInstance().remove(taskId);
        } catch (Exception e) {
            LOGGER.error("remove task from _RUNNING_TASK_FROM_TASK_NODE error.[taskId:{}]", taskId, e);
        }
    }

    /**
     * get running tasks from node cache
     *
     * @param nodeId
     * @return
     */
    public Set<Long> getTasksByNodeId(Long nodeId) {
        return _NODE_TASK_CACHE.get(nodeId);
    }

    /**
     * get number of running tasks from node cache
     *
     * @param nodeId
     * @return
     */
    public int getTaskCountByNodeId(Long nodeId) {
        Set<Long> tasks = _NODE_TASK_CACHE.get(nodeId);
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    /**
     * Clear all cache.
     * <p>This method will be called when node become not leader</p>
     */
    public void clearAllCache() {
        _NODE_CACHE.clear();
        _NODE_TASK_CACHE.clear();
    }

}

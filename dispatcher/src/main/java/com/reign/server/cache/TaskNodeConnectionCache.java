package com.reign.server.cache;

import com.reign.component.utils.Cache.ExpireCache;
import com.reign.component.utils.Cache.ExpireCallBack;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 16-2-3.
 */
public class TaskNodeConnectionCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskNodeConnectionCache.class);

    private static final TaskNodeConnectionCache _INSTANCE = new TaskNodeConnectionCache();

    private static final ExpireCache<String, Channel> _TASK_NODE_CONNECTION_CACHE = ExpireCache
            .setExpireTime(60, 30, TimeUnit.SECONDS, false)
            .build(new ExpireCallBack() {
                public Object handler(Object key) throws Exception {
                    LOGGER.error("[Disconnect] TaskNode disconnected form NameNode.[taskNode:{}]", key);
                    _TASK_NODE_CONNECTION_CACHE.remove(key.toString());
                    return null;
                }
            });

//    private static final Cache<String, Channel> _TASK_NODE_CONNECTION_CACHE = CacheBuilder.newBuilder()
//            .expireAfterWrite(30, TimeUnit.SECONDS)
//            .removalListener(new RemovalListener<Object, Object>() {
//                public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
//                    //if remove reason is timeOut
//                    if (removalNotification.getCause().equals(RemovalCause.EXPIRED)) {
//                        LOGGER.error("[Disconnect] TaskNode disconnected form NameNode.[taskNode:{}]", removalNotification.getKey());
//                    }
//                }
//            }).build();

    private TaskNodeConnectionCache() {
    }

    public static TaskNodeConnectionCache getInstance() {
        return _INSTANCE;
    }

    public void add(String nodeName, Channel channel) {
        _TASK_NODE_CONNECTION_CACHE.put(nodeName, channel);
    }

    public Channel getChannel(String nodeName) {
        return _TASK_NODE_CONNECTION_CACHE.get(nodeName);
    }

}

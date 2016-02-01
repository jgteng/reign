package com.reign.server.cache;

import com.reign.server.domain.CacheTaskNodeGroupInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 15-12-14.
 */
public class NodeGroupCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeGroupCache.class);

    private static final NodeGroupCache _INSTANCE = new NodeGroupCache();

    //running tasks in this NodeGroup
    private static final ConcurrentHashMap<Long, Set<Long>> _NODE_GROUP_TASK_CACHE = new ConcurrentHashMap<Long, Set<Long>>();

    //NodeGroup info cache
    private static final ConcurrentHashMap<Long, CacheTaskNodeGroupInfo> _NODE_GROUP_CACHE = new ConcurrentHashMap<Long, CacheTaskNodeGroupInfo>();

    private NodeGroupCache() {
    }

    public static NodeGroupCache getInstance() {
        return _INSTANCE;
    }

    public void addTask(Long groupId, Long taskId) {
        synchronized (_NODE_GROUP_TASK_CACHE) {
            Set<Long> tasks = _NODE_GROUP_TASK_CACHE.get(groupId);
            if (tasks == null) {
                tasks = new HashSet<Long>();
                _NODE_GROUP_TASK_CACHE.put(groupId, tasks);
            }
            tasks.add(taskId);
        }
    }

    public void removeTask(Long groupId, Long taskId) {
        synchronized (_NODE_GROUP_TASK_CACHE) {
            Set<Long> tasks = _NODE_GROUP_TASK_CACHE.get(groupId);
            if (tasks != null) {
                tasks.remove(taskId);
            }
        }
    }

    public Set<Long> getTasksByGroupId(Long groupId) {
        return _NODE_GROUP_TASK_CACHE.get(groupId);
    }

    public int getTasksCountByGroupId(Long groupId) {
        Set<Long> tasks = _NODE_GROUP_TASK_CACHE.get(groupId);
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    /**
     * Clear all cache.
     * <p>This method will be called when node become no leader</p>
     */
    public void clearAllCache() {
        _NODE_GROUP_TASK_CACHE.clear();
        _NODE_GROUP_CACHE.clear();
    }
}

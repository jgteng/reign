package com.reign.server.cache;

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

    private static final ConcurrentHashMap<Long, Set<Long>> nodeGroupTaskCache = new ConcurrentHashMap<Long, Set<Long>>();

    private NodeGroupCache() {
    }

    private static final NodeGroupCache _INSTANCE = new NodeGroupCache();

    public static NodeGroupCache getInstance() {
        return _INSTANCE;
    }

    public void addTask(Long groupId, Long taskId) {
        synchronized (nodeGroupTaskCache) {
            Set<Long> tasks = nodeGroupTaskCache.get(groupId);
            if (tasks == null) {
                tasks = new HashSet<Long>();
                nodeGroupTaskCache.put(groupId, tasks);
            }
            tasks.add(taskId);
        }
    }

    public void removeTask(Long groupId, Long taskId) {
        synchronized (nodeGroupTaskCache) {
            Set<Long> tasks = nodeGroupTaskCache.get(groupId);
            if (tasks != null) {
                tasks.remove(taskId);
            }
        }
    }

    public Set<Long> getTasksByGroupId(Long groupId) {
        return nodeGroupTaskCache.get(groupId);
    }

    public int getTasksCountByGroupId(Long groupId) {
        synchronized (nodeGroupTaskCache) {
            Set<Long> tasks = nodeGroupTaskCache.get(groupId);
            if (tasks == null) {
                return 0;
            }
            return tasks.size();
        }
    }
}

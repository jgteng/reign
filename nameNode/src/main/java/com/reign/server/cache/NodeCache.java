package com.reign.server.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 15-12-14.
 */
public class NodeCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCache.class);

    private static final ConcurrentHashMap<String, Set<Long>> nodeTaskCache = new ConcurrentHashMap<String, Set<Long>>();

    private static final NodeCache _INSTANCE = new NodeCache();

    private NodeCache() {
    }

    public static NodeCache getInstance() {
        return _INSTANCE;
    }

    public void addTask(String nodeName, Long taskId) {
        synchronized (nodeTaskCache) {
            Set<Long> tasks = nodeTaskCache.get(nodeName);
            if (tasks == null) {
                tasks = new HashSet<Long>();
                nodeTaskCache.put(nodeName, tasks);
            }
            tasks.add(taskId);
        }
    }

    public void removeTask(String nodeName, Long taskId) {
        synchronized (nodeTaskCache) {
            Set<Long> tasks = nodeTaskCache.get(nodeName);
            if (tasks != null) {
                tasks.remove(taskId);
            }
        }
    }

    public Set<Long> getTasksByNodeId(String nodeName) {
        return nodeTaskCache.get(nodeName);
    }

    public int getTaskCountByNodeId(String nodeName) {
        synchronized (nodeTaskCache) {
            Set<Long> tasks = nodeTaskCache.get(nodeName);
            if (tasks == null) {
                return 0;
            }
            return tasks.size();
        }
    }

}

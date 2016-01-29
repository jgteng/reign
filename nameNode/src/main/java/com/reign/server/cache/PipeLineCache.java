package com.reign.server.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-1-29.
 */
public class PipeLineCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(PipeLineCache.class);

    //max task count that can be pulled by TaskNode every time
    private static final int TASK_COUNT_BATCH = 10;

    private static final PipeLineCache _PIPE_LINE_CACHE = new PipeLineCache();

    private static final ConcurrentHashMap<String, Set<Long>> _NODE_TASK_CACHE = new ConcurrentHashMap<String, Set<Long>>(100);

    private PipeLineCache() {
    }

    public static PipeLineCache getInstance() {
        return _PIPE_LINE_CACHE;
    }

    public void addTaskToNode(String nodeName, Long taskId) {
        synchronized (_NODE_TASK_CACHE) {
            if (_NODE_TASK_CACHE.contains(nodeName)) {
                Set<Long> tmpSet = _NODE_TASK_CACHE.get(nodeName);
                tmpSet.add(taskId);
            } else {
                Set<Long> tmpSet = new HashSet<Long>();
                tmpSet.add(taskId);
                _NODE_TASK_CACHE.put(nodeName, tmpSet);
            }
        }
        LOGGER.debug("add to pipeline success");
    }

    public List<Long> getTasksByNodeName(String nodeName) {
        List<Long> taskIds = null;
        if (_NODE_TASK_CACHE.contains(nodeName)) {
            Set<Long> taskSet = _NODE_TASK_CACHE.get(nodeName);
            if (taskSet != null && taskSet.size() > 0) {
                taskIds = new ArrayList<Long>();
                Iterator<Long> iterator = taskSet.iterator();
                while (iterator.hasNext()) {
                    if (taskIds.size() >= TASK_COUNT_BATCH) {
                        break;
                    }
                    taskIds.add(iterator.next());
                }
            }
        }
        return taskIds;
    }

}

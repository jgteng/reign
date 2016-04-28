package com.reign.client.cache;

import com.reign.domain.runtime.RunTimeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-2-2.
 */
public class RunningTaskCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningTaskCache.class);

    private static RunningTaskCache runingTaskCache = new RunningTaskCache();

    //key: taskId, value:RunTimeBean
    private static final ConcurrentHashMap<Long, RunTimeBean> _RUNNIG_TASK__CACHE = new ConcurrentHashMap<Long, RunTimeBean>();

    private RunningTaskCache() {
    }

    public static RunningTaskCache getInstance() {
        return runingTaskCache;
    }

    public void addTask(RunTimeBean runTimeBean) {
        _RUNNIG_TASK__CACHE.put(runTimeBean.getTaskId(), runTimeBean);
    }

    public void removeTask(Long taskId) {
        _RUNNIG_TASK__CACHE.remove(taskId);
    }

    public Set<Long> getTaskIds() {
        return _RUNNIG_TASK__CACHE.keySet();
    }

}

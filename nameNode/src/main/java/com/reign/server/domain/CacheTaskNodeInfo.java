package com.reign.server.domain;

import com.reign.domain.task.TaskNode;

/**
 * TaskNode info stored in NameNode cache
 * <p/>
 * Created by ji on 16-1-28.
 */
public class CacheTaskNodeInfo extends TaskNode {
    //the time that added to cache
    private Long cacheTime;

    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }
}

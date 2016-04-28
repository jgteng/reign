package com.reign.server.domain;

import com.reign.domain.task.TaskNodeGroup;

/**
 * Created by ji on 16-1-28.
 */
public class CacheTaskNodeGroupInfo extends TaskNodeGroup {
    //time added to cache
    private Long cacheTime;

    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }
}

package com.reign.server.dao;

import com.reign.server.domain.CacheTaskNodeInfo;

/**
 * Created by ji on 16-1-28.
 */
public class TaskNodeDao extends BaseDao{

    /**
     * query taskNode info from db by id
     * @param nodeId
     * @return
     * @throws Exception
     */
    public CacheTaskNodeInfo getTaskNodeById(Long nodeId) throws Exception {
        return SqlMapperManager.getSqlSession().selectOne("getTaskNodeById", nodeId);
    }
}

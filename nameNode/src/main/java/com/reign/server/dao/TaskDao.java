package com.reign.server.dao;


import com.reign.domain.task.Task;

import java.util.List;

/**
 * Created by ji on 15-10-8.
 */
public class TaskDao {

    public List<Task> selectTaskList() {
        return SqlMapperManager.getSqlSession().selectList("selectTaskList");
    }
}

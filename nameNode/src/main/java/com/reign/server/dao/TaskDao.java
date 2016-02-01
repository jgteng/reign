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

    public int updateTaskToQueue(Task task) {
        return SqlMapperManager.getSqlSession().update("updateTaskToQueue", task);
    }

    public Task getTaskById(Long taskId) {
        return SqlMapperManager.getSqlSession().selectOne("getTaskById", taskId);
    }

    public List<Task> getQueueRunTask() {
        return SqlMapperManager.getSqlSession().selectList("getQueueRunTask");
    }

    public int changeTaskStatus(Task task) {
        return SqlMapperManager.getSqlSession().update("updateTaskStatus", task);
    }
}

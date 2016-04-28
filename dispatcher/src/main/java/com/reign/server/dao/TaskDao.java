package com.reign.server.dao;


import com.reign.domain.task.Task;

import java.util.List;

/**
 * Created by ji on 15-10-8.
 */
public class TaskDao extends BaseDao {

    public List<Task> selectTaskList() {
        return super.selectList("selectTaskList");
    }

    public int updateTaskToQueue(Task task) {
        return super.update("updateTaskToQueue", task);
    }

    public Task getTaskById(Long taskId) {
        return super.selectOne("getTaskById", taskId);
    }

    public List<Task> getQueueRunTask() {
        return super.selectList("getQueueRunTask");
    }

    public int changeTaskStatus(Task task) {
        return super.update("updateTaskStatus", task);
    }
}

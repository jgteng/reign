package com.reign.manager.dao;

import com.reign.domain.task.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ji on 15-10-15.
 */
public interface TaskDao {
    List<Task> listTask();

    /**
     * insert a new task
     * @param paramTask
     */
    void addTask(Task paramTask);

    /**
     * update task info
     * @param paramTask
     */
    void modifyTask(Task paramTask);

    Long countByName(@Param("taskName") String taskName);

    /**
     * query task by id
     * @param taskId
     * @return
     */
    Task getById(@Param("taskId")Long taskId);

    /**
     * delete task by id
     * @param taskId
     */
    void delTask(@Param("taskId")Long taskId);
}

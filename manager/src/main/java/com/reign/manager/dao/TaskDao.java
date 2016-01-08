package com.reign.manager.dao;

import com.reign.domain.task.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ji on 15-10-15.
 */
public interface TaskDao {
    List<Task> listTask();

    void addTask(Task paramTask);

    void modifyTask(Task paramTask);

    Long countByName(@Param("taskName") String taskName);

    /**
     * 根据id查询
     * @param taskId
     * @return
     */
    Task getById(@Param("taskId")Long taskId);

    /**
     * 根据id删除任务
     * @param taskId
     */
    void delTask(@Param("taskId")Long taskId);
}

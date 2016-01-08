package com.reign.manager.service.task;

import com.reign.domain.task.Task;

import java.util.List;

/**
 * Created by ji on 15-10-15.
 */
public interface TaskService {
    List<Task> listTask();

    Long saveTask(Task paramTask) throws Exception;

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    Task getById(Long id) throws Exception;

    /**
     * 根据id删除任务
     * @param id
     */
    void delTask(Long id) throws Exception;
}

package com.reign.server.cache;

import com.reign.domain.task.Task;
import com.reign.server.dao.DaoFactory;
import com.reign.server.dao.TaskDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 15-12-14.
 */
public class TaskCache {
    public static final Logger LOGGER = LoggerFactory.getLogger(TaskCache.class);

    private static final TaskCache _TASK_CACHE = new TaskCache();

    private static final ConcurrentHashMap<Long, Task> _TASK_MAP = new ConcurrentHashMap<Long, Task>();

    private TaskCache() {
    }

    public static TaskCache getInstance() {
        return _TASK_CACHE;
    }

    public void addTask(Task task) {
        _TASK_MAP.put(task.getId(), task);
    }

    public void removeTask(Long taskId) {
        _TASK_MAP.remove(taskId);
    }

    public Task getTask(Long taskId) {
        Task task = null;
        try {
            task = _TASK_MAP.get(taskId);
            if (task == null) {
                TaskDao taskDao = (TaskDao) DaoFactory.getDao(TaskDao.class);
                task = taskDao.getTaskById(taskId);
            }
        } catch (Exception e) {
            LOGGER.error("Get task from TaskCache error [taskId:{}]", taskId, e);
        }
        return task;
    }
}

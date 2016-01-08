package com.reign.manager.service.task;

import com.reign.aop.annotation.ParamCheck;
import com.reign.component.utils.CronExpressionUtil;
import com.reign.domain.task.Task;
import com.reign.manager.dao.TaskDao;
import com.reign.manager.exception.ReignManagerException;
import com.reign.manager.web.ResponseCodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ji on 15-10-15.
 */
@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskDao taskDao;

    @ParamCheck
    public List<Task> listTask() {
        return taskDao.listTask();
    }

    public Long saveTask(Task paramTask) throws Exception {
        if (paramTask.getId() == null) {
            Date nextTime = CronExpressionUtil.getNextTime(paramTask.getTaskRule());
            paramTask.setNextTime(nextTime);

            Long count = taskDao.countByName(paramTask.getTaskName());
            if (count >= 1) {
                throw new ReignManagerException(ResponseCodeConstants.DUPLICATE_ERROR_CODE, "存在同名任务");
            }
            taskDao.addTask(paramTask);
        } else {
            taskDao.modifyTask(paramTask);
        }
        return paramTask.getId();
    }

    public Task getById(Long id) throws Exception {
        return taskDao.getById(id);
    }

    public void delTask(Long id) throws Exception {
        taskDao.delTask(id);
    }
}

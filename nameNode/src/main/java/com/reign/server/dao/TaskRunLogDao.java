package com.reign.server.dao;

import com.reign.domain.task.TaskRunLog;

/**
 * Created by ji on 16-1-29.
 */
public class TaskRunLogDao extends BaseDao {
    public void addTaskRunLog(TaskRunLog taskRunLog) {
        super.insert("addTaskRunLog", taskRunLog);
    }

    public void changeStatus(TaskRunLog taskRunLog) {
        super.update("changeStatus", taskRunLog);
    }
}

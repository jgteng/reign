package com.reign.server.rpc.handler;

import com.alibaba.fastjson.JSONArray;
import com.reign.component.constants.CoreConstant;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.rpc.TaskRunStatusRpcData;
import com.reign.domain.task.Task;
import com.reign.domain.task.TaskRunLog;
import com.reign.server.cache.NodeCache;
import com.reign.server.cache.NodeGroupCache;
import com.reign.server.cache.PipeLineCache;
import com.reign.server.cache.TaskCache;
import com.reign.server.dao.DaoFactory;
import com.reign.server.dao.TaskDao;
import com.reign.server.dao.TaskRunLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 16-2-2.
 */
public class TaskStatusMessageHandler implements MessageHandlerInf {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusMessageHandler.class);

    @Override
    public String handleMessage(NTMessageProtocol messageProtocol) {
        JSONArray statusArray = messageProtocol.getArrayData();
        if (statusArray == null || statusArray.size() > 0) {
            for (int i = 0; i < statusArray.size(); i++) {
                TaskRunStatusRpcData taskStatusData = null;
                try {
                    taskStatusData = statusArray.getObject(i, TaskRunStatusRpcData.class);
                    if (taskStatusData != null) {
                        Integer status = taskStatusData.getStatus();
                        if (status == null) {
                            throw new RuntimeException("Task status is null");
                        }
                        switch (status.intValue()) {
                            case CoreConstant.TASK_STATUS_RUN:
                                this.changeTaskStatusToRun(taskStatusData);
                                break;
                            case CoreConstant.TASK_STATUS_WAIT:
                                this.taskRunSuccess(taskStatusData);
                                break;
                            case CoreConstant.TASK_STATUS_FAIL:
                                this.taskRunFail(taskStatusData);
                                break;
                            default:
                                throw new RuntimeException("Task status is invalid:[status:" + status + "]");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("[Protocol {}] Handle task status error.{}", new Object[]{MessageTypeConstant.TASK_STATUS_TYPE, taskStatusData}, e);
                }
            }
        }
        return null;
    }


    private void changeTaskStatusToRun(TaskRunStatusRpcData taskStatusData) throws Exception {
        Long taskId = taskStatusData.getTaskId();
        Task task = TaskCache.getInstance().getTask(taskId);
        if (task == null) {
            LOGGER.error("Can not get task info when change task status to run. [taskId:{}]", taskId);
            return;
        }

        /* remove from pipeline cache */
        PipeLineCache.getInstance().removeTask(task.getRunNodeName(), taskId);
        task.setStatus(CoreConstant.TASK_STATUS_RUN);
        TaskCache.getInstance().addTask(task);

        TaskDao taskDao = DaoFactory.getDao(TaskDao.class);
        taskDao.changeTaskStatus(task);
        TaskRunLogDao taskRunLogDao = DaoFactory.getDao(TaskRunLogDao.class);

        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setId(taskStatusData.getLogId());
        taskRunLog.setStatus(CoreConstant.TASK_STATUS_RUN);
        taskRunLog.setStartTime(taskStatusData.getTime());
        taskRunLogDao.changeStatus(taskRunLog);
    }

    private void taskRunSuccess(TaskRunStatusRpcData taskStatusData) throws Exception {
        Long taskId = taskStatusData.getTaskId();
        Task task = TaskCache.getInstance().getTask(taskId);
        if (task == null) {
            LOGGER.error("Can not get task info when change task status to success. [taskId:{}]", taskId);
            return;
        }
        task.setStatus(CoreConstant.TASK_STATUS_WAIT);

        /*** remove from cache start  ***/
        TaskCache.getInstance().removeTask(taskId);
        NodeCache.getInstance().removeTask(task.getRunNodeId(), taskId);
        if (task.getNodeType() != null && task.getNodeType().equals(CoreConstant.NODE_TYPE_VIRTUAL)) {
            NodeGroupCache.getInstance().removeTask(task.getNodeId(), taskId);
        }
        /*** remove from cache end  ***/


        TaskDao taskDao = DaoFactory.getDao(TaskDao.class);
        taskDao.changeTaskStatus(task);
        TaskRunLogDao taskRunLogDao = DaoFactory.getDao(TaskRunLogDao.class);

        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setId(taskStatusData.getLogId());
        taskRunLog.setStatus(CoreConstant.TASK_STATUS_WAIT);
        taskRunLog.setEndTime(taskStatusData.getTime());
        taskRunLogDao.changeStatus(taskRunLog);
    }

    private void taskRunFail(TaskRunStatusRpcData taskStatusData) throws Exception {
        //todo task can retry when failed
        Long taskId = taskStatusData.getTaskId();
        Task task = TaskCache.getInstance().getTask(taskId);
        if (task == null) {
            LOGGER.error("Can not get task info when change task status to fail. [taskId:{}]", taskId);
            return;
        }
        task.setStatus(CoreConstant.TASK_STATUS_FAIL);

        /*** remove from cache start  ***/
        TaskCache.getInstance().removeTask(taskId);
        NodeCache.getInstance().removeTask(task.getRunNodeId(), taskId);
        if (task.getNodeType() != null && task.getNodeType().equals(CoreConstant.NODE_TYPE_VIRTUAL)) {
            NodeGroupCache.getInstance().removeTask(task.getNodeId(), taskId);
        }
        /*** remove from cache end  ***/

        TaskDao taskDao = DaoFactory.getDao(TaskDao.class);
        taskDao.changeTaskStatus(task);
        TaskRunLogDao taskRunLogDao = DaoFactory.getDao(TaskRunLogDao.class);

        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setId(taskStatusData.getLogId());
        taskRunLog.setStatus(CoreConstant.TASK_STATUS_FAIL);
        taskRunLog.setEndTime(taskStatusData.getTime());
        taskRunLogDao.changeStatus(taskRunLog);
    }
}

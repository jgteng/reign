package com.reign.server.thread.executors;

import com.reign.component.constants.CoreConstant;
import com.reign.component.utils.CronExpressionUtil;
import com.reign.domain.task.Task;
import com.reign.domain.task.TaskRunLog;
import com.reign.server.cache.NodeCache;
import com.reign.server.cache.PipeLineCache;
import com.reign.server.cache.RunningTasksFromTaskNodeCache;
import com.reign.server.cache.TaskCache;
import com.reign.server.dao.DaoFactory;
import com.reign.server.dao.TaskDao;
import com.reign.server.dao.TaskRunLogDao;
import com.reign.server.domain.CacheTaskNodeInfo;
import com.reign.server.thread.template.ThreadTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by ji on 15-9-29.
 */
public class TaskScanThread extends ThreadTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScanThread.class);
    private TaskDao taskDao = DaoFactory.getDao(TaskDao.class);
    private TaskRunLogDao taskRunLogDao = new TaskRunLogDao();

    @Override
    public void run() {
        List<Task> taskList = taskDao.selectTaskList();
        if (taskList != null) {
            this.allocateTasks(taskList);
        }
    }

    /**
     * Assign task to the right taskNode
     *
     * @param taskList
     */
    private void allocateTasks(List<Task> taskList) {
        for (Task task : taskList) {
            if (task != null) {
                boolean isStillRunning = this.doRunningCheck(task);
                if (isStillRunning) {
                    LOGGER.warn("Task is still running, can not be run again!!");
                    continue;
                }
                String nodeType = task.getNodeType();
                if (nodeType.equals(CoreConstant.NODE_TYPE_PHYSICAL)) {
                    //this method will invoke if task allocate on physical node
                    this.handleTaskWithPhysicalNode(task);
                } else if (nodeType.equals(CoreConstant.NODE_TYPE_VIRTUAL)) {
                    //this method will invoke if task allocate on node group
                    this.handleTaskWithVirtualNode(task);
                } else {
                    LOGGER.error("Task [taskId:{},taskName:{}] have invalid node type [nodeType:{}], escape.", new Object[]{task.getId(), task.getTaskName(), task.getNodeType()});
                }
            }
        }
    }

    /**
     * check task is still running or not
     *
     * @param task
     * @return running: true, not: false
     */
    private boolean doRunningCheck(Task task) {
        boolean isRunning = false;
        if (RunningTasksFromTaskNodeCache.getInstance().containsKey(task.getId())) {
            isRunning = true;
        }
        return isRunning;
    }

    /**
     * Handle task allocated to physical node
     *
     * @param task
     */
    private void handleTaskWithPhysicalNode(Task task) {
        if (task.getNodeId() == null) {
            LOGGER.error("Task [taskId:{},taskName:{}] nodeId is null, escape this running.", task.getId(), task.getTaskName());
        }
        CacheTaskNodeInfo taskNodeInfo = NodeCache.getInstance().getNode(task.getNodeId());
        if (taskNodeInfo == null) {
            LOGGER.error("Can not get TaskNode [nodeId:{}] to run task [taskId:{}]", task.getNodeId(), task.getId());
            return;
        }

        //Determine whether running task count is over limit
        if (taskNodeInfo.getTaskLimit() != null && taskNodeInfo.getTaskLimit().intValue() > 0) {
            int taskCountIsRunning = NodeCache.getInstance().getTaskCountByNodeId(task.getNodeId());
            if (taskCountIsRunning >= taskNodeInfo.getTaskLimit().intValue()) {
                LOGGER.info("Task Node is over limit [TaskNodeId: {}], [TaskNodeIp:{}]", taskNodeInfo.getId(), taskNodeInfo.getIp());
                return;
            }
        }

        this.generateTaskRunningInfo(task, taskNodeInfo);
        //update task status=queue,nextTime,run node info
        int successCount = taskDao.updateTaskToQueue(task);
        if (successCount <= 0) {
            LOGGER.error("Update task status to 'queue' error [taskId:{}]", task.getId());
            return;
        }

        //add task to TaskNode running list
        NodeCache.getInstance().addTask(task.getNodeId(), task.getId());
        //add task to pipeline
        PipeLineCache.getInstance().addTaskToNode(task.getRunNodeName(), task.getId());
        //add task to TaskCache
        TaskCache.getInstance().addTask(task);
    }

    private void generateTaskRunningInfo(Task task, CacheTaskNodeInfo taskNodeInfo) {
        task.setRunNodeId(taskNodeInfo.getId());
        task.setRunNodeName(taskNodeInfo.getNodeName());
        task.setStatus(CoreConstant.TASK_STATUS_QUEUE);
        task.setLastRunTime(new Date());
        Date nextTime = null;
        try {
            nextTime = CronExpressionUtil.getNextTime(task.getTaskRule());
        } catch (Exception e) {
            LOGGER.error("calculate next run time error [taskRule:]");
        }
        task.setNextTime(nextTime);

        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setTaskId(task.getId());
        taskRunLog.setTaskName(task.getTaskName());
        taskRunLog.setStatus(CoreConstant.TASK_STATUS_QUEUE);
        taskRunLog.setQueueTime(new Date());
        taskRunLogDao.addTaskRunLog(taskRunLog);

        task.setRunLogId(taskRunLog.getId());
    }

    /**
     * Handle task allocated to node group
     *
     * @param task
     */
    private void handleTaskWithVirtualNode(Task task) {
        Long groupId = task.getNodeId();
    }

}

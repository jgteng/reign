package com.reign.server.thread.executors;

import com.reign.component.constants.CoreConstant;
import com.reign.domain.task.Task;
import com.reign.server.cache.NodeCache;
import com.reign.server.dao.TaskDao;
import com.reign.server.thread.template.ThreadTemplate;

import java.util.List;

/**
 * Created by ji on 15-9-29.
 */
public class TaskScanThread extends ThreadTemplate {

    @Override
    public void run() {
        TaskDao taskDao = new TaskDao();
        List<Task> taskList = taskDao.selectTaskList();
        if (taskList != null) {
            this.allocateTasks(taskList);
        }
    }

    private void allocateTasks(List<Task> taskList) {
        for (Task task : taskList) {
            if (task != null) {
                String nodeType = task.getNodeType();
                if (nodeType.equals(CoreConstant.NODE_TYPE_PHYSICAL)) {
                    this.handleTaskWithPhysicalNode(task);
                } else if (nodeType.equals(CoreConstant.NODE_TYPE_VIRTUAL)) {
                    this.handleTaskWithVirtualNode(task);
                }
            }
        }
    }

    private void handleTaskWithPhysicalNode(Task task) {

        NodeCache.getInstance().addTask(task.getNodeName(), task.getId());

    }

    private void handleTaskWithVirtualNode(Task task) {

    }

}

package com.reign.server.main;

import com.reign.component.constants.CoreConstant;
import com.reign.domain.task.Task;
import com.reign.server.cache.NodeCache;
import com.reign.server.cache.NodeGroupCache;
import com.reign.server.cache.PipeLineCache;
import com.reign.server.cache.TaskCache;
import com.reign.server.core.alive.AliveCheckZk;
import com.reign.server.dao.DaoFactory;
import com.reign.server.dao.TaskDao;
import com.reign.server.rpc.socket.NettyServer;
import com.reign.server.thread.executors.TaskScanThread;
import com.reign.server.thread.template.SimpleScheduleThread;
import com.reign.server.thread.template.WorkThreadControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ji on 15-9-25.
 * System Entrance
 */
public class StartUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    private static SimpleScheduleThread taskThread;

    public static void main(String[] args) {
        try {
            System.setProperty("org.terracotta.quartz.skipUpdateCheck", "true");

            AliveCheckZk.regist("127.0.0.1:2181", "reign", "nameNodes", "node1");

            //init druid data source
//            ConnectionManager.init();

            TaskDao taskDao = (TaskDao) DaoFactory.getDao(TaskDao.class);
            List<Task> taskList = taskDao.selectTaskList();
            System.out.println(taskList.size());

            //start socket server
            NettyServer nettyServer = NettyServer.getInstance();
            nettyServer.init();

            taskThread = new SimpleScheduleThread("taskThread", 60, TaskScanThread.class);

            taskThread.start();

        } catch (Exception e) {
            LOGGER.error("Start Error,System exit ", e);
            System.exit(-1);
        }

    }

    /**
     * init tasks when NameNode become online and is leader
     */
    public static void initTask() {
        TaskDao taskDao = (TaskDao) DaoFactory.getDao(TaskDao.class);
        List<Task> taskList = taskDao.getQueueRunTask();
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                if (task != null && task.getStatus() != null) {
                    TaskCache.getInstance().addTask(task);
                    NodeCache.getInstance().addTask(task.getNodeId(), task.getId());
                    //If task status is 'queue', then add task to PipeLine
                    if (task.getStatus().intValue() == CoreConstant.TASK_STATUS_QUEUE) {
                        PipeLineCache.getInstance().addTaskToNode(task.getRunNodeName(), task.getId());
                    }

                    //if TaskNode type is 'virtual', then add task to NodeGroupCache
                    if (task.getNodeType() != null && !task.getNodeType().equals(CoreConstant.NODE_TYPE_VIRTUAL)) {
                        NodeGroupCache.getInstance().addTask(task.getNodeId(), task.getId());
                    }
                }
            }
        }
    }

    /**
     * called before system exit
     */
    public void systemExit() {
        //stop workThread
        WorkThreadControl.stopWorkThread();
        //close zk connection,destroy watches
        AliveCheckZk.close();
    }
}

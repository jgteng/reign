package com.reign.server.main;

import com.reign.domain.task.Task;
import com.reign.server.core.alive.AliveCheckZk;
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

            TaskDao taskDao = new TaskDao();
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
     * called before system exit
     */
    public void systemExit() {
        //stop workThread
        WorkThreadControl.stopWorkThread();
        //close zk connection,destroy watches
        AliveCheckZk.close();
    }
}

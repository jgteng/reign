package com.reign.client.main;

import com.alibaba.fastjson.JSONObject;
import com.reign.client.rpc.socket.NettyClient;
import com.reign.client.thread.executors.PullTaskThread;
import com.reign.client.thread.template.SimpleScheduleThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 15-9-29.
 */
public class StartUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    private static SimpleScheduleThread taskPullThread;

    public static void main(String[] args) {
        try {
            NettyClient nettyClient = NettyClient.getInstance();
            nettyClient.init();

            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("type", "123");
            jsonObject.put("data", "测试数据");
//            nettyClient.sendMessage(jsonObject.toJSONString());

            taskPullThread = new SimpleScheduleThread("taskPullThread", 10, PullTaskThread.class);
            taskPullThread.start();

//            for (int i = 0; i < 100; i++) {
//
//                nettyClient.sendMessage(jsonObject.toJSONString());
//                Thread.sleep(5000L);
//
//            }
        } catch (Exception e) {
            LOGGER.error("TaskNode startUp failed", e);
        }
    }
}

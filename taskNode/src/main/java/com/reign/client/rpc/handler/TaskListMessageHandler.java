package com.reign.client.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reign.client.core.TaskRunner;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.runtime.RunTimeBean;
import com.reign.domain.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ji on 16-1-15.
 */
public class TaskListMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskListMessageHandler.class);

    public String handleMessage(NTMessageProtocol messageProtocol) {
        JSONObject data = messageProtocol.getData();
        if (data != null) {
            JSONArray jsonArray = data.getJSONArray("list");
            if (jsonArray != null && jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Task task = JSON.toJavaObject(object, Task.class);
                    this.handleTask(task);
                }
            }
        }
        return null;
    }

    private void handleTask(Task task) {
        try {
            RunTimeBean runTimeBean = new RunTimeBean();
            runTimeBean.setTaskId(task.getId());
            runTimeBean.setTaskName(task.getTaskName());
            runTimeBean.setMainScript(task.getMainScript());
            runTimeBean.setScriptPath("/tmp/");
            new Thread(new TaskRunner(runTimeBean), "TASK_RUNNER_THREAD_" + task.getTaskName()).start();
        } catch (Throwable t) {
            LOGGER.error("Start thread to run task error", t);
        }

    }
}

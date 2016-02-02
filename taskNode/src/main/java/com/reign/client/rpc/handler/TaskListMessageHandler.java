package com.reign.client.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reign.client.core.TaskRunner;
import com.reign.client.dao.SqliteDaoFactory;
import com.reign.component.constants.CoreConstant;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.rpc.TaskRunStatusRpcData;
import com.reign.domain.runtime.RunTimeBean;
import com.reign.domain.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by ji on 16-1-15.
 */
public class TaskListMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskListMessageHandler.class);

    public String handleMessage(NTMessageProtocol messageProtocol) {
        JSONArray resultDataArray = new JSONArray();

        JSONArray jsonArray = messageProtocol.getArrayData();
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Task task = JSON.toJavaObject(object, Task.class);
                if (task != null && task.getRunLogId() != null) {
                    try {
                        SqliteDaoFactory.getInstance().insertTaskStatus(task.getRunLogId());
                    } catch (SQLException e) {
                        LOGGER.error("Insert status into sqlite error [logId:{},taskId:{}]", task.getRunLogId(), task.getId());
                    }
                }
                TaskRunStatusRpcData resultData = this.handleTask(task);
                jsonArray.add(JSON.toJSON(resultData));
            }
        }
        String resultStr = this.generateResultMessage(resultDataArray);
        return resultStr;
    }


    private TaskRunStatusRpcData handleTask(Task task) {
        TaskRunStatusRpcData resultData = new TaskRunStatusRpcData();
        resultData.setTaskId(task.getId());
        resultData.setLogId(task.getRunLogId());
        resultData.setTime(new Date());
        try {
            RunTimeBean runTimeBean = new RunTimeBean();
            runTimeBean.setTaskId(task.getId());
            runTimeBean.setTaskName(task.getTaskName());
            runTimeBean.setMainScript(task.getMainScript());
            runTimeBean.setScriptPath("/tmp/");
            new Thread(new TaskRunner(runTimeBean), "TASK_RUNNER_THREAD_" + task.getTaskName()).start();

            resultData.setStatus(CoreConstant.TASK_STATUS_RUN);
        } catch (Throwable t) {
            resultData.setStatus(CoreConstant.TASK_STATUS_FAIL);
            resultData.setMsg(t.getMessage());
            LOGGER.error("Start thread to run task error.[taskId:{}]", task.getId(), t);
        }
        return resultData;
    }

    private String generateResultMessage(JSONArray resultDataArray) {
        NTMessageProtocol ntMessageProtocol = new NTMessageProtocol();
        ntMessageProtocol.setType(MessageTypeConstant.TASK_TAKEN_TYPE);
        ntMessageProtocol.setArrayData(resultDataArray);
        return ntMessageProtocol.toString();
    }
}

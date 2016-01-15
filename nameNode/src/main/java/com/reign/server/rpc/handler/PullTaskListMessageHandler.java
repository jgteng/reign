package com.reign.server.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.task.Task;
import com.reign.server.dao.TaskDao;

import java.util.List;

/**
 * Created by ji on 16-1-15.
 */
public class PullTaskListMessageHandler {

    private static TaskDao taskDao;
    static {
        taskDao = new TaskDao();
    }

    public String handleMessage(NTMessageProtocol messageProtocol) {
        NTMessageProtocol result = new NTMessageProtocol();

        JSONObject resultObj = new JSONObject();
        List<Task> taskList = taskDao.selectTaskList();
        result.setType(MessageTypeConstant.TASK_PULL_RESULT_TYPE);

        resultObj.put("list", taskList);
        result.setData(resultObj);

        return result.toString();
    }
}

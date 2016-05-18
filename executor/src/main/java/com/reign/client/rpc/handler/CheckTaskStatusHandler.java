package com.reign.client.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reign.client.dao.SqliteDaoFactory;
import com.reign.client.domain.TaskRunStatus;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.rpc.TaskRunStatusRpcData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * Created by ji on 16-2-14.
 */
public class CheckTaskStatusHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckTaskStatusHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String handleMessage(NTMessageProtocol messageProtocol) {
        String resultMessage = null;
        try {
            JSONObject data = messageProtocol.getData();
            Long logId = data.getLong("logId");
            Long taskId = data.getLong("taskId");

            LOGGER.debug("=====【executor】获取任务状态，taskId:{},logId:{}", taskId, logId);

            TaskRunStatus taskRunStatus = SqliteDaoFactory.getInstance().getRecentStatusInfoByLogId(logId.toString());
            if (taskRunStatus != null) {
                NTMessageProtocol ntMessageProtocol = new NTMessageProtocol();
                ntMessageProtocol.setType(MessageTypeConstant.TASK_STATUS_TYPE);

                TaskRunStatusRpcData runStatusRpcData = new TaskRunStatusRpcData();
                runStatusRpcData.setLogId(logId);
                runStatusRpcData.setTaskId(taskId);
                runStatusRpcData.setStatus(Integer.parseInt(taskRunStatus.getStatus()));
                runStatusRpcData.setTime(sdf.parse(taskRunStatus.getStatusTimeStr()));

                JSONArray dataArray = new JSONArray();
                dataArray.add(JSON.parseObject(JSON.toJSONString(runStatusRpcData)));
                ntMessageProtocol.setArrayData(dataArray);

                resultMessage = ntMessageProtocol.toString();
            }

            LOGGER.debug("======【executor】返回获取任务状态：{}", resultMessage);
        } catch (Exception e) {
            LOGGER.error("[CheckTaskStatusHandler] error", e);
        }

        return resultMessage;
    }
}

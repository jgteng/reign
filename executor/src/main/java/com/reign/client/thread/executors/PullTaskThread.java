package com.reign.client.thread.executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reign.client.rpc.socket.NettyClient;
import com.reign.client.thread.template.ThreadTemplate;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 15-11-11.
 */
public class PullTaskThread extends ThreadTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(PullTaskThread.class);

    @Override
    public void run() {
        LOGGER.debug("TaskNode拉取任务");
        String requestMessage = this.createPullTaskRequestMessage();
        NettyClient.getInstance().sendMessage(requestMessage);
    }

    private String createPullTaskRequestMessage() {
        NTMessageProtocol messageProtocol = new NTMessageProtocol();
        messageProtocol.setType(MessageTypeConstant.TASK_PULL_TYPE);

        JSONObject dataObj = new JSONObject();
        dataObj.put("nodeId", "test_node_1");
        messageProtocol.setData(dataObj);

        return JSON.toJSONString(messageProtocol);
    }
}

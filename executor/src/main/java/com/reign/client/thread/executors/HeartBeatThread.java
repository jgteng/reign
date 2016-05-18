package com.reign.client.thread.executors;

import com.alibaba.fastjson.JSONObject;
import com.reign.client.core.SysStatusInfo;
import com.reign.client.main.StartUp;
import com.reign.client.rpc.socket.NettyClient;
import com.reign.client.thread.template.ThreadTemplate;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 16-5-18.
 */
public class HeartBeatThread extends ThreadTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatThread.class);

    @Override
    public void run() {
        try {
            if (SysStatusInfo.getInstance().isAuthSuccess()) {
                String heatBeat = buildHeatBeat();
                LOGGER.debug("Client send heart beat messsage to server : ---> " + heatBeat);
                NettyClient.getInstance().sendMessage(heatBeat);
            }
        } catch (Throwable t) {
            LOGGER.error("write heart beat message erro", t);
        }
    }

    private String buildHeatBeat() {
        NTMessageProtocol ntMessageProtocol = new NTMessageProtocol();
        ntMessageProtocol.setType(MessageTypeConstant.HEART_BEAT_TYPE);
        JSONObject message = new JSONObject();
        message.put("node", StartUp.nodeName);
        ntMessageProtocol.setData(message);

        return ntMessageProtocol.toString();
    }
}

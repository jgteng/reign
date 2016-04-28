package com.reign.server.rpc.handler;

import com.alibaba.fastjson.JSONArray;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.server.cache.RunningTasksFromTaskNodeCache;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ji on 16-2-3.
 */
public class TaskRunningListHandler implements MessageHandlerInf {
    public String handleMessage(NTMessageProtocol messageProtocol, ChannelHandlerContext ctx) {
        String nodeName = messageProtocol.getSource();
        if (nodeName != null) {
            JSONArray array = messageProtocol.getArrayData();
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    Long taskId = array.getLong(i);
                    RunningTasksFromTaskNodeCache.getInstance().add(taskId, nodeName);
                }
            }
        }
        return null;
    }

}

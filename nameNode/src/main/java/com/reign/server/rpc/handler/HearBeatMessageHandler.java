package com.reign.server.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.server.cache.TaskNodeConnectionCache;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ji on 16-2-3.
 */
public class HearBeatMessageHandler implements MessageHandlerInf{
    @Override
    public String handleMessage(NTMessageProtocol messageProtocol,ChannelHandlerContext ctx) {
        JSONObject dataObj = messageProtocol.getData();
        String nodeName = dataObj.getString("node");
        TaskNodeConnectionCache.getInstance().add(nodeName, ctx.channel());
        return null;
    }
}

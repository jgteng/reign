package com.reign.server.rpc.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.server.cache.TaskNodeConnectionCache;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ji on 15-9-29.
 */
public class LoginAuthResponseHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject message = JSON.parseObject(msg.toString());
        if (message.getInteger("type") == null) {
            ctx.fireChannelRead(msg);
        } else if (message.getInteger("type") == MessageTypeConstant.LOGIN_AUTH_REQ_TYPE) {
            String nodeName = message.getString("node");

            //todo check whether taskNode can connected to NameNode

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", MessageTypeConstant.LOGIN_AUTH_RESPONSE_TYPE);
            jsonObject.put("value", MessageTypeConstant.LOGIN_AUTH_SUCCESS);

            //Add channel to connection cache
            TaskNodeConnectionCache.getInstance().add(nodeName, ctx.channel());

            //send response to taskNode
            ctx.writeAndFlush(jsonObject.toJSONString());
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}

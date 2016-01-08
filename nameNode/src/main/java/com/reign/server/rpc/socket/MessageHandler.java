package com.reign.server.rpc.socket;

import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 15-9-29.
 */
public class MessageHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        JSONObject jsonObject = JSONObject.parseObject(msg.toString());
        jsonObject.put("back", "backMessage");

        NTMessageProtocol messageProtocol = JSONObject.toJavaObject(jsonObject, NTMessageProtocol.class);
        if (messageProtocol.getType().intValue() == MessageTypeConstant.TASK_PULL_TYPE) {
            String nodeId = ((JSONObject) messageProtocol.getData()).getString("nodeId");
            System.out.println(nodeId);
        }
        ctx.channel().writeAndFlush(jsonObject.toJSONString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("SOCKET SERVER[MessageHandler] @exceptionCaught", cause);
    }
}

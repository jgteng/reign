package com.reign.client.rpc.socket;

import com.alibaba.fastjson.JSON;
import com.reign.client.rpc.handler.TaskListMessageHandler;
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
    private static final TaskListMessageHandler taskListMessageHandler = new TaskListMessageHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NTMessageProtocol messageProtocol = JSON.toJavaObject(JSON.parseObject(msg.toString()), NTMessageProtocol.class);
        if (messageProtocol == null || messageProtocol.getType() == null) {
            LOGGER.error("[Message info error] Can not process message without type");
            return;
        }

        String resultMessage = "";
        switch (messageProtocol.getType().intValue()) {
            case MessageTypeConstant.TASK_PULL_RESULT_TYPE:
                resultMessage = taskListMessageHandler.handleMessage(messageProtocol);
                break;
        }
        ctx.channel().writeAndFlush(resultMessage);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("SOCKET CLIENT[MessageHandler] @exceptionCaught", cause);
    }
}

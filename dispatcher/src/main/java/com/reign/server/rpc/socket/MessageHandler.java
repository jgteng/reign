package com.reign.server.rpc.socket;

import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.server.cache.TaskNodeConnectionCache;
import com.reign.server.rpc.handler.HearBeatMessageHandler;
import com.reign.server.rpc.handler.PullTaskListMessageHandler;
import com.reign.server.rpc.handler.TaskRunningListHandler;
import com.reign.server.rpc.handler.TaskStatusMessageHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 15-9-29.
 */
public class MessageHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private static final HearBeatMessageHandler heartBeatMessageHandler = new HearBeatMessageHandler();

    private static final PullTaskListMessageHandler taskListMessageHandler = new PullTaskListMessageHandler();

    private static final TaskStatusMessageHandler taskStatusMessageHandler = new TaskStatusMessageHandler();

    private static final TaskRunningListHandler taskRunningListHandler = new TaskRunningListHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(msg.toString());
        }

        JSONObject jsonObject = JSONObject.parseObject(msg.toString());
        NTMessageProtocol messageProtocol = JSONObject.toJavaObject(jsonObject, NTMessageProtocol.class);
        if (messageProtocol.getType() == null) {
            LOGGER.error("[Message info error] Can not process message without type");
            return;
        }

        String resultMessage = "";
        switch (messageProtocol.getType().intValue()) {
            case MessageTypeConstant.HEART_BEAT_TYPE:
                heartBeatMessageHandler.handleMessage(messageProtocol, ctx);
                break;
            case MessageTypeConstant.TASK_PULL_TYPE:
                resultMessage = taskListMessageHandler.handleMessage(messageProtocol, ctx);
                break;
            case MessageTypeConstant.TASK_STATUS_TYPE:
                resultMessage = taskStatusMessageHandler.handleMessage(messageProtocol, ctx);
                break;
            case MessageTypeConstant.SEND_RUNNING_TASKS_TYPE:
                resultMessage = taskRunningListHandler.handleMessage(messageProtocol, ctx);
                break;
        }

        if (resultMessage != null && !"".equals(resultMessage)) {
            ctx.channel().writeAndFlush(resultMessage);
        }
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

package com.reign.client.rpc.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.MessageTypeConstant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 15-9-29.
 * 向NameNode发送验证请求:
 * 如果验证通过则开始发送心跳信息;
 * 如果验证失败,系统退出
 */
public class LoginAUthRequestHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAUthRequestHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String message = this.buildLoginRequestMessage();
        ByteBuf messageBytes = Unpooled.copiedBuffer(message.getBytes());
        ctx.writeAndFlush(messageBytes);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject message = JSON.parseObject(msg.toString());

        if (message.getInteger("type") == null) {
            ctx.fireChannelRead(msg);
        } else if (message.getInteger("type") == MessageTypeConstant.LOGIN_AUTH_RESPONSE_TYPE) {
            switch (message.getInteger("value")) {
                case MessageTypeConstant.LOGIN_AUTH_SUCCESS:
                    LOGGER.info("So check Login Auth Succeed");

                    //验证连接成功后开始发心跳
                    ctx.executor().scheduleAtFixedRate(new HeartbeatTask(ctx), 0, 3, TimeUnit.SECONDS);

                    break;
                case MessageTypeConstant.LOGIN_AUTH_FAIL:
                    LOGGER.error("Socket Login Auth Fail");
                    ctx.close();
                    LOGGER.error("Connect to NameNode failed,System exit! Please Check This TaskNode Is Permitted By NameNode!!!!");
                    System.exit(-1);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    private String buildLoginRequestMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", MessageTypeConstant.LOGIN_AUTH_REQ_TYPE);
        return jsonObject.toJSONString();
    }

    /**
     * 发送心跳逻辑
     */
    private class HeartbeatTask implements Runnable {
        private ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void run() {
            String heatBeat = buildHeatBeat();
            LOGGER.debug("Client send heart beat messsage to server : ---> " + heatBeat);
            ByteBuf resp = Unpooled.copiedBuffer(heatBeat.getBytes());
            ctx.writeAndFlush(resp);
        }

        private String buildHeatBeat() {
            JSONObject message = new JSONObject();
            message.put("type", MessageTypeConstant.HEART_BEAT_TYPE);
            return message.toJSONString();
        }
    }
}

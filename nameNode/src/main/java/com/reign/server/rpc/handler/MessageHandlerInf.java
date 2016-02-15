package com.reign.server.rpc.handler;

import com.reign.domain.rpc.NTMessageProtocol;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ji on 16-2-2.
 */
public interface MessageHandlerInf {
    public String handleMessage(NTMessageProtocol messageProtocol,ChannelHandlerContext ctx);
}

package com.reign.server.rpc.handler;

import com.reign.domain.rpc.NTMessageProtocol;

/**
 * Created by ji on 16-2-2.
 */
public interface MessageHandlerInf {
    public String handleMessage(NTMessageProtocol messageProtocol);
}

package com.reign.client.rpc.socket;

import com.reign.client.config.ClientConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


/**
 * Created by ji on 15-9-29.
 */
public class NettyClient implements MessageClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
    private static ChannelFuture channelFuture;
    private static volatile NettyClient nettyClient;

    private NettyClient() {
    }

    public static NettyClient getInstance() {
        if (nettyClient == null) {
            synchronized (LOGGER) {
                if (nettyClient == null) {
                    nettyClient = new NettyClient();
                }
            }
        }
        return nettyClient;
    }

    public void init() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAUthRequestHandler());
                            ch.pipeline().addLast(new MessageHandler());
                        }
                    });

            channelFuture = bootstrap.connect(new InetSocketAddress(ClientConstant.SERVER_IP, ClientConstant.SERVER_PORT),
                    new InetSocketAddress(ClientConstant.CLIENT_IP, ClientConstant.CLIENT_PORT)).sync();

            LOGGER.info("TaskNode Socket Client Start Successfully At Port " + ClientConstant.CLIENT_PORT);
        } catch (Exception e) {
            if (channelFuture.channel().isActive()) {
                return;
            }
            LOGGER.error("Connect to Server failed", e);
            try {
                TimeUnit.SECONDS.sleep(5);
                init();
            } catch (Exception ex) {
                LOGGER.error("Connect to Server failed,retry", ex);
            }
        }
    }

    public synchronized void sendMessage(String message) {

        try {
            if (!channelFuture.channel().isActive()) {
                LOGGER.error("Client Server is Not Connected,and try to reconnect");
                init();
            }
            ByteBuf resp = Unpooled.copiedBuffer(message.getBytes());
            ChannelFuture future = channelFuture.channel().writeAndFlush(resp);
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable e) {
            LOGGER.error("Message send failed:", message, e);
//            this.reConnect();
        }
    }

}

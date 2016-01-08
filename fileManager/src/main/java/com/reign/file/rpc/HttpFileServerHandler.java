package com.reign.file.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * User: ji
 * Date: 15-8-28
 * Time: 上午8:50
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;
    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //如果解码错误,放回BAD_REQUEST
        if (!fullHttpRequest.getDecoderResult().isSuccess()) {
            sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        //如果不是get方式,则返回METHOD_NOT_ALLOWED
        if (fullHttpRequest.getMethod() != HttpMethod.GET) {
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = fullHttpRequest.getUri();
        final String path = sanitizeUri(uri);

        if (path == null) {
            sendError(channelHandlerContext, HttpResponseStatus.NOT_FOUND);
            return;
        }

        File file = new File(path);
        //如果是隐藏文件,或者是不存在的文件,直接返回不存在异常
        if (file.isHidden() || !file.exists()) {
            sendError(channelHandlerContext, HttpResponseStatus.NOT_FOUND);
            return;
        }

        //如果文件是个目录
        if (file.isDirectory()) {
            //如果是个目录,且以"/"结尾,则返回文件清单
            if (uri.endsWith("/")) {
                sendListing(channelHandlerContext, file);
            } else {
                sendRedirect(channelHandlerContext, uri + "/");
            }
            return;
        }

        //如果访问的不是一个文件,则直接返回FORBIDDEN
        if (!file.isFile()) {
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }

        RandomAccessFile randomAccessFile;

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            sendError(channelHandlerContext, HttpResponseStatus.NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpHeaders.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        if (HttpHeaders.isKeepAlive(fullHttpRequest)) {
            response.headers().set("CONNECTION", HttpHeaders.Values.KEEP_ALIVE);
        }

        channelHandlerContext.write(response);
        ChannelFuture sendFileFuture;
        sendFileFuture = channelHandlerContext.write(
                new ChunkedFile(randomAccessFile, 0, fileLength, 8192),
                channelHandlerContext.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {
                if (total < 0) {
                    System.err.println("Transfer progress : " + progress);
                } else {
                    System.err.println("Transfer progress: " + progress + "/" + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("Transfer complete.");
            }
        });

        ChannelFuture lastContentFuture = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpHeaders.isKeepAlive(fullHttpRequest)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode("uri", "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }

        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }

        uri = uri.replace("/", File.separator);
        if (uri.contains(File.separator + ".")
                || uri.contains("." + File.separator)
                || uri.startsWith("." + File.separator)
                || uri.endsWith(".")
                ||INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        return uri;
//        return System.getProperty("user.dir") + File.separator + uri;
    }

    private static void sendListing(ChannelHandlerContext ctx, File file) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
        StringBuilder builder = new StringBuilder();
        String dirPath = file.getPath();
        builder.append("<!DOCTYPE html>\r\n");
        builder.append("<html><head><title>");
        builder.append(dirPath);
        builder.append("</title></head><body>");
        builder.append("<h3>Listing of: ");
        builder.append(dirPath);
        builder.append("</h3>\r\n");

        builder.append("<ul>");
        builder.append("<li><a href=\"../\">..</a></li>\r\n");

        StringBuilder rowBuilder = new StringBuilder();
        for (File f : file.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }

            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }


            DecimalFormat df   = new DecimalFormat("######0.00");
            int KB = 1024;
            int MB = 1048576;
            int GB = 1073741824;

            double fileLen = f.length();
            String fileLenStr = fileLen + " KB";
            if (fileLen > KB) {
                if (fileLen > GB) {
                    fileLenStr = df.format((f.length()) * 0.1 / (GB)) + " GB";
                } else if (fileLen > MB) {
                    fileLenStr = df.format((f.length()) * 0.1 / MB) + " MB";
                }
            }

            int tmpStrLen = fileLenStr.length();
            if (tmpStrLen < 18) {
                for (int i = 0; i < (18 - tmpStrLen); i++) {
                    fileLenStr = fileLenStr + "&nbsp;";
                }
            }
            fileLenStr.replaceAll(" ", "&nbsp;");

            String liStr = "";
            if (f.isDirectory()) {
                liStr += "<li>文件夹&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"";
            } else {
                liStr += "<li>" + fileLenStr + "<a href=\"";
            }
            liStr += name;
            liStr+=("\">");
            liStr+=name;
            liStr += "</a></li>\r\n";
            if (f.isDirectory()) {
                rowBuilder.insert(0, liStr);
            } else {
                rowBuilder.append(liStr);
            }
        }

        builder.append(rowBuilder);
        builder.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(builder, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUrl) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, newUrl);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }



    private void sendError(ChannelHandlerContext channelHandlerContext, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8)
        );

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getPath()));
    }
}

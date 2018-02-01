package com.ning.netty.demo.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by ning on 2017/12/21.
 * User:ning
 * Date:2017/12/21
 * tIME:16:06
 */
public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
    static String WEBROOT = new File("").getAbsolutePath();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if(HttpUtil.is100ContinueExpected(request)){
            send100Continue(ctx,request);
        }
        boolean keepAlive  = HttpUtil.isKeepAlive(request);
        String  uri        = request.uri();
        byte[]  content    = ("not found you uri" + new Random().nextInt()).getBytes(CharsetUtil.UTF_8);
        ByteBuf contentBuf = Unpooled.wrappedBuffer(content);
        RandomAccessFile raf = new RandomAccessFile(new File(WEBROOT + uri),"r");
        ByteBuffer buf = ByteBuffer.allocate((int)raf.getChannel().size());
        raf.getChannel().read(buf);
        contentBuf = Unpooled.wrappedBuffer(buf.array());

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, contentBuf);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        if(keepAlive){
            response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }else{
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }


    }
    public void send100Continue(ChannelHandlerContext ctx,FullHttpRequest request){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

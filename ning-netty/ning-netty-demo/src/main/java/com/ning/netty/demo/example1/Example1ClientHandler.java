package com.ning.netty.demo.example1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * Created by ning on 2018/2/3.
 * User:ning
 * Date:2018/2/3
 * tIME:11:03
 */
public class Example1ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String content = "from server :" + ctx.channel().remoteAddress() + ":" + msg;
        System.out.println(content);
        ctx.writeAndFlush(UUID.randomUUID().toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client active");
        ctx.writeAndFlush(UUID.randomUUID().toString());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

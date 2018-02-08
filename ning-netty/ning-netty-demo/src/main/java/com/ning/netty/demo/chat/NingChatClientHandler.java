package com.ning.netty.demo.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by ning on 2018/2/8.
 * User:ning
 * Date:2018/2/8
 * tIME:14:04
 */
public class NingChatClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) throws Exception {
        ex.printStackTrace();
        ctx.close();
    }
}

package com.ning.netty.demo.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by ning on 2018/2/8.
 * User:ning
 * Date:2018/2/8
 * tIME:14:04
 */
public class NingChatServerHandler extends SimpleChannelInboundHandler<String> {

    static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        group.forEach(ch ->{
            if(channel != ch){
                ch.writeAndFlush(channel.remoteAddress() + " 发送消息:" + msg + "\n");
            }else{
                ch.writeAndFlush("[自己]:" + msg + "\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush("[服务器]- " + channel.remoteAddress() + "加入\n");
        group.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush("[服务器] - " + channel.remoteAddress() + "下线\n");
        //默认会自动移除下线的channel 这个api可以不调用 系统会自动处理
        group.remove(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) throws Exception {
        ex.printStackTrace();
        ctx.close();
    }
}

package com.ning.netty.demo.protobuf;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:13:59
 */
@ChannelHandler.Sharable
public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonEntity.Person content = (PersonEntity.Person)msg;
        System.out.println("server receive:" + content.getEmail());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //回写给客户端
        PersonEntity.Person entity = PersonEntity.Person.newBuilder()
                .setId(1)
                .setEmail("client@126.com")
                .setName("client-u")
                .build();
        ctx.writeAndFlush(entity).addListener(ChannelFutureListener.CLOSE);

    }

    /*@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active....");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive...");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered...");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered...");
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

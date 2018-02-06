package com.ning.netty.demo.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:14:31
 */
@ChannelHandler.Sharable
public class ProtoBufClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PersonEntity.Person entity = PersonEntity.Person.newBuilder()
                .setId(1)
                .setEmail("client@126.com")
                .setName("client-u")
                .build();
        ctx.channel().writeAndFlush(entity);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonEntity.Person content = (PersonEntity.Person)msg;
        System.out.println("client receive:" + content.getName());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        System.out.println("============> 扑抓到异常");
        cause.printStackTrace();
        ctx.close();
    }


}

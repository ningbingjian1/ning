package com.ning.netty.demo.readwrite.check;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by ning on 2018/2/8.
 * User:ning
 * Date:2018/2/8
 * tIME:14:04
 */
public class NingReadWriteCheckServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String eventType = null ;
        Channel channel = ctx.channel();
        if(evt instanceof IdleStateEvent){
            System.out.println(((IdleStateEvent) evt).state());
            switch (((IdleStateEvent) evt).state()){
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
            }
            System.out.println(channel.remoteAddress() +" 超时事件:" + eventType);

        }
    }
}

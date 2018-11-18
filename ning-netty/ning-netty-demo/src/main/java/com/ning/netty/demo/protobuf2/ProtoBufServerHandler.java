package com.ning.netty.demo.protobuf2;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import static com.ning.netty.demo.protobuf2.RpcMsgProto.*;


/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:13:59
 */
@ChannelHandler.Sharable
public class ProtoBufServerHandler extends SimpleChannelInboundHandler<RpcMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsgProto.RpcMsg msg) throws Exception {
        if(msg.getMsgType() == RpcMsg.MsgType.PersonType){
            System.out.println("PersonType :" + msg.getPerson().toString() );
        }else if(msg.getMsgType() == RpcMsg.MsgType.CarType){
            System.out.println("CarType :" + msg.getCar().toString() );
        }else if(msg.getMsgType() == RpcMsg.MsgType.PointType){
            System.out.println("PointType :" + msg.getPoint().toString() );
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

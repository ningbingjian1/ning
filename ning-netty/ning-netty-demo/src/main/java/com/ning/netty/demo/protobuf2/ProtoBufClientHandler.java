package com.ning.netty.demo.protobuf2;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;
import static com.ning.netty.demo.protobuf2.RpcMsgProto.*;

/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:14:31
 */
@ChannelHandler.Sharable
public class ProtoBufClientHandler extends SimpleChannelInboundHandler<RpcMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Random random = new Random();
        int i = random.nextInt(3) + 1;
        RpcMsg msg = null ;
        if(i == 1 ){
            Person person = Person.newBuilder().setName("u1").setEmail("tv@1216.com").setId(1).build();
            msg = RpcMsg.newBuilder().setMsgType(RpcMsg.MsgType.PersonType).setPerson(person).build();
        }else if (i == 2 ){
            Car car = Car.newBuilder().setName("bw").setPrice(100).build();
            msg = RpcMsg.newBuilder().setMsgType(RpcMsg.MsgType.CarType).setCar(car).build();
        }else if (i == 3 ){
            Point point = Point.newBuilder().setX(100).setY(200).build();
            msg = RpcMsg.newBuilder().setMsgType(RpcMsg.MsgType.PointType).setPoint(point).build();
        }
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        System.out.println("============> 扑抓到异常");
        cause.printStackTrace();
        ctx.close();
    }


}

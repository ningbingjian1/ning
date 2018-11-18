package com.ning.netty.demo.example1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by ning on 2018/2/3.
 * User:ning
 * Date:2018/2/3
 * tIME:10:49
 */
public class Example1Client {
    static int port = 999;
    static String host = "localhost";
    public static void main(String[] args) throws Exception{
        EventLoopGroup  bossGroup     = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new Exampl1ClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
        }


    }
}

package com.ning.netty.demo.example1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by ning on 2018/2/3.
 * User:ning
 * Date:2018/2/3
 * tIME:10:49
 */
public class Example1Server {
    static int port = 999;
    public static void main(String[] args) throws Exception{
        EventLoopGroup  bossGroup     = new NioEventLoopGroup();
        EventLoopGroup  workerGroup     = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new Example1ServerInitializer());
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}

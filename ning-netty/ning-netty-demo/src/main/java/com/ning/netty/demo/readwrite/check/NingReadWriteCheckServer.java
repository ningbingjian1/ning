package com.ning.netty.demo.readwrite.check;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:11:32
 */
public class NingReadWriteCheckServer {
    private int port ;
    public NingReadWriteCheckServer(int port) {
        this.port = port;
    }
    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new NingReadWriteCheckServerInitializer());
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(NingReadWriteCheckServer.class.getSimpleName() + " starting and listening on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)throws Exception {
      new NingReadWriteCheckServer(999).start();
    }



}

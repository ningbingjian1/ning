package com.ning.netty.demo.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by ning on 2017/12/21.
 * User:ning
 * Date:2017/12/21
 * tIME:16:02
 */
public class HttpServer {
    private EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    public ChannelFuture start(InetSocketAddress address){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer());
        ChannelFuture future = serverBootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }
    public void destroy(){
        System.out.println("destroy");
        if(channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }
    public static void main(String[] args) {
        final HttpServer server = new HttpServer();
        ChannelFuture    future = server.start(new InetSocketAddress(8899));
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.destroy();
            }
        });
        ChannelFuture closeFuture = future.channel().closeFuture();
        closeFuture.syncUninterruptibly();
        System.out.println("done");
    }

}

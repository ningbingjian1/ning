package com.ning.netty.demo.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:11:32
 */
public class NingEchoServer {
    private int port ;
    public NingEchoServer(int port) {
        this.port = port;
    }
    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        final ChannelHandler serverHandler = new NingEchoServerHandler();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(NingEchoServer.class.getSimpleName() + " starting and listening on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)throws Exception {
      /*  if(args.length != 1 ){
            System.err.println("useage:" + NingEchoServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.valueOf(args[0]);*/
      new NingEchoServer(999).start();
    }



}

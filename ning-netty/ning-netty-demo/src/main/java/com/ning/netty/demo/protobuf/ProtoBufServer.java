package com.ning.netty.demo.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;

/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:11:32
 */
public class ProtoBufServer {
    private int port ;
    public ProtoBufServer(int port) {
        this.port = port;
    }
    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        final ChannelHandler serverHandler = new ProtoBufServerHandler();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new ProtobufDecoder(PersonEntity.Person.getDefaultInstance()));
                            pipeline.addLast(new ProtoBufServerHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(ProtoBufServer.class.getSimpleName() + " starting and listening on " + f.channel().localAddress());
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
      new ProtoBufServer(999).start();
    }



}

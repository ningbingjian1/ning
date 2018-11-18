package com.ning.netty.demo.readwrite.check;

import com.ning.netty.demo.chat.NingChatClientInitializer;
import com.ning.netty.demo.echo.NingEchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * Created by ning on 2017/12/16.
 * User:ning
 * Date:2017/12/16
 * tIME:11:32
 */
public class NingReadWriteCheckClient {
    private final String host = "localhost";
    private final int port = 999;

    public void start()throws Exception{
        final ChannelHandler clientHandler = new NingEchoClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new NingChatClientInitializer());
            ChannelFuture f = b.connect().sync();
            Channel channel = f.channel();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                //f.channel获取的是服务端的通道
                channel.writeAndFlush(reader.readLine() +"\r\n");
            }
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new NingReadWriteCheckClient().start();
    }
}

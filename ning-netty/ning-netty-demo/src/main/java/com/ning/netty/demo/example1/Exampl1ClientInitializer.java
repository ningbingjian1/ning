package com.ning.netty.demo.example1;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * Created by ning on 2018/2/3.
 * User:ning
 * Date:2018/2/3
 * tIME:11:14
 */
public class Exampl1ClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //针对长度的解码  maxFrameLength 最大长度   lengthFieldOffset 表示长度起始位置  lengthFieldLength 表示长度的字节个数
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        // 针对长度的编码器
        pipeline.addLast(new LengthFieldPrepender(4));
        //字符串编解码
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //自己的解码器
        pipeline.addLast(new Example1ClientHandler());
    }
}

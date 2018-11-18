package com.ning.netty.demo.readwrite.check;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by ning on 2018/2/8.
 * User:ning
 * Date:2018/2/8
 * tIME:13:56
 */
public class NingReadWriteCheckServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //解码行分隔符
        pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
        //解码字符串
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //读空闲检测  写空闲检测  读写空闲检测
        pipeline.addLast(new IdleStateHandler(3,5,10, TimeUnit.SECONDS));
        pipeline.addLast(new NingReadWriteCheckServerHandler());

    }
}

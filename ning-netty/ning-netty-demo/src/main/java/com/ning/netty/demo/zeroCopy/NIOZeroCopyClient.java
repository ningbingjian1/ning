package com.ning.netty.demo.zeroCopy;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 这个程序在windows 10 测试好像有点问题 没法接收到正常的数据   在Linux运行没问题
 */
public class NIOZeroCopyClient {
    public static void main(String[] args)throws Exception {
       SocketChannel socketChannel = SocketChannel.open();
       socketChannel.connect(new InetSocketAddress("localhost",9981));
       socketChannel.configureBlocking(true);

       String filepath = args[0];
       FileChannel fileChannel =  new FileInputStream(filepath).getChannel();
       long transferCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);
       System.out.println("客户端发送:" + transferCount);
       fileChannel.close();

   /*    long total = 0 ;
       for(int i = 0 ;i < 10 ;i ++){

           FileChannel fileChannel =  new FileInputStream(new File(filepath)).getChannel();
           System.out.println("fileChannel.size() = " + fileChannel.size());
           long transferCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);
           total += transferCount;
           System.out.println(transferCount);
           fileChannel.close();
       }
        System.out.println("客户端传输结束:total = " + Double.valueOf(total)/1024/1024 + "M");
        socketChannel.close();;*/

    }
}

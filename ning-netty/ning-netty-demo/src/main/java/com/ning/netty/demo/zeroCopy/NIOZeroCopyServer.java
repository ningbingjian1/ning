package com.ning.netty.demo.zeroCopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 这个程序在windows 10 测试好像有点问题 没法接收到正常的数据   在Linux运行没问题
 */
public class NIOZeroCopyServer {
    public static void main(String[] args)throws Exception {
        InetSocketAddress addr = new InetSocketAddress(9981);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocketChannel.bind(addr);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while(true){
            int read = 0 ;
            int total = 0 ;
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);
            while(read != -1 ){
                try{
                    read = socketChannel.read(buf);
                }catch (Exception ex){
                    ex.printStackTrace();
                    read = -1;
                    continue;
                }
                total += read;
                //这里一定要加上rewind  否则从下次开始是都不到数据的
                buf.rewind();

            }
            System.out.println("接收到客户端总共:total = " + Double.valueOf(total)/1024/1024 + "M");
        }
    }
}

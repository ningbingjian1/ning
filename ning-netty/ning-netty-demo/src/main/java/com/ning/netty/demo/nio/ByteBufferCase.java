package com.ning.netty.demo.nio;


import java.nio.ByteBuffer;
import java.util.Random;

/**
 * ByteBuffer读写的例子
 */
public class ByteBufferCase {
    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        for(int i = 0 ; i < 10 ;i ++){
            //每次往buf写入数据都要调用clean
            buf.clear();
            write(10,buf);
            buf.flip();
            read(buf);
        }

    }
    static void read(ByteBuffer buf){
            while(buf.hasRemaining()){
                System.out.print(buf.get() + "\t");
            }
        System.out.println();
    }
    static void write(int writes,ByteBuffer buf){
        Random random = new Random();
        char tab = '\t';
        for(int i = 0 ; i < writes;i++){
           buf.put((byte)random.nextInt(100));
        }
        System.out.println("----------------------------------------------------------------------------");
    }
}

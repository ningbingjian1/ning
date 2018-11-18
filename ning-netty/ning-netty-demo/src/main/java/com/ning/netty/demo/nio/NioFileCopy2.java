package com.ning.netty.demo.nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * 这是一个使用NIO API 拷贝文件的例子
 */
public class NioFileCopy2 {
    public static void main(String[] args)throws Exception {
        File infile = new File("ning-netty/ning-netty-demo/fileIn2.txt") ;
        File outfile = new File("ning-netty/ning-netty-demo/fileOut2.txt") ;
        FileInputStream fis = new FileInputStream(infile);
        FileOutputStream fos = new FileOutputStream(outfile);
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int reads = fisChannel.read(buf);
        //flip clear 一般成对出现
        while(reads != -1  ){
            buf.flip();
            while(buf.hasRemaining()){
                fosChannel.write(buf);
            }
            //如果没有clear方法，reads每次都返回0，因为possition == limit,所以这个循环没办法停止  一直输出
            buf.clear();
            reads = fisChannel.read(buf);
        }
        fos.close();
        fis.close();

    }
}

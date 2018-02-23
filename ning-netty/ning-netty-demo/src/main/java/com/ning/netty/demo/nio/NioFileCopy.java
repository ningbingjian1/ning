package com.ning.netty.demo.nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 这是一个使用NIO API 拷贝文件的例子
 */
public class NioFileCopy {
    public static void main(String[] args)throws Exception {
        File infile = new File("ning-netty/ning-netty-demo/fileIn.txt") ;
        File outfile = new File("ning-netty/ning-netty-demo/fileOut.txt") ;
        FileInputStream fis = new FileInputStream(infile);
        FileOutputStream fos = new FileOutputStream(outfile);
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        inChannel.read(buf);
        buf.flip();
        //针对中文的问题 这里可以是UTF-8 也可以是ISO8859-1，输出的文件不会乱码，但是控制台如果不用UTF-8
        //那就回乱码
        //Charset charset = Charset.forName("GBK");
        Charset charset = Charset.forName("ISO8859-1");
        CharBuffer charBuffer = charset.decode(buf);
        //控制台打印是乱码
        System.out.println(charBuffer);
        ByteBuffer outBuf  = charset.encode(charBuffer);
        outChannel.write(outBuf);
        fis.close();

    }
}

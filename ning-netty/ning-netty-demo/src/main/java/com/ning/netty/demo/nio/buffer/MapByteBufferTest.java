package com.ning.netty.demo.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MapByteBufferTest {
    public static void main(String[] args) throws Exception {
        //test1();

    }



    /**
     *
     * @throws IOException
     */
    private static void test1() throws IOException {
        int length = 10;//一个byte占1B，所以共向文件中存128M的数据
        RandomAccessFile raf =
                new RandomAccessFile("ning-netty/ning-netty-demo/mbb.txt","rw");
        MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE,0,length);
        for(int i=0;i<length;i++) {
            mbb.put((byte)i);
        }
        for(int i = 0;i<length;i++) {
            //像数组一样访问
            System.out.println(mbb.get(i));
        }
    }
}

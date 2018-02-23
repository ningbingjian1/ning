package com.ning.netty.demo.nio;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 示例程序 将数据写入sink
 * 然后从source 读取数据
 */
public class PipeTest {
    static ExecutorService service = Executors.newCachedThreadPool();
    public static void main(String[] args)  throws Exception{
        Pipe pipe = Pipe.open();
        Random random = new Random();
        Charset charset = Charset.forName("UTF-8");
        Pipe.SinkChannel sink = pipe.sink();
        Pipe.SourceChannel source = pipe.source();
        service.submit(() ->{
            while(true){
                TimeUnit.SECONDS.sleep(1);
                String content = "number:" + random.nextInt(100);
                byte[]bytes = content.getBytes(charset);
                ByteBuffer buf = ByteBuffer.allocate(bytes.length);
                buf.put(bytes);
                buf.flip();
                sink.write(buf);
            }
        });
        service.submit(() ->{
            while(true){
                TimeUnit.SECONDS.sleep(1);
                ByteBuffer buf = ByteBuffer.allocate(1024);
                source.read(buf);
                buf.flip();
                CharBuffer cb = charset.decode(buf);
                System.out.println(cb);
            }
        });


    }

}

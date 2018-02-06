package com.ning.kafka.util;

import org.jboss.netty.util.CharsetUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/6
 * Time: 19:37
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class ConsumerTest {
    static ExecutorService service = Executors.newSingleThreadExecutor();
    public static void main(String[] args) throws Exception{
        KafkaTemplate template1 = KafkaTemplate.getInstance("19e_1");
        template1.startConsumer((a,b) ->{
            String record =  new String(b, CharsetUtil.UTF_8);
            System.out.println(record);
        });
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}

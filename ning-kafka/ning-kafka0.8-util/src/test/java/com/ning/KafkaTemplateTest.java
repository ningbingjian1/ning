package com.ning;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.ning.kafka.util.KafkaTemplate;
import org.jboss.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Created by ning on 2018/2/6.
 * User:ning
 * Date:2018/2/6
 * tIME:18:52
 */
public class KafkaTemplateTest {
    static int RECORDS_NUM = 5000;
    @Test
    public void testProducer() throws Exception{
        KafkaTemplate template1 = KafkaTemplate.getInstance("19e_1");
        KafkaTemplate template2 = KafkaTemplate.getInstance("19e_2");

        for(int i = 0 ;i < RECORDS_NUM;i ++){
            template1.send("a".getBytes(CharsetUtil.UTF_8),("ning-t1-" + i).getBytes(CharsetUtil.UTF_8));
            template2.send("a".getBytes(CharsetUtil.UTF_8),("ning-t2-" + i).getBytes(CharsetUtil.UTF_8));
        }

        Thread.sleep(20000);

    }
    @Test
    public void testConsumer()throws Exception{
        KafkaTemplate template1 = KafkaTemplate.getInstance("19e_1");
        BufferedWriter bw = Files.newWriter(new File("record.txt"),Charsets.UTF_8);
        template1.startConsumer((a,b) ->{
            String record =  new String(b,CharsetUtil.UTF_8);
            try {
                bw.write(record + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        while(true){
            Thread.sleep(100);
        }
    }

}

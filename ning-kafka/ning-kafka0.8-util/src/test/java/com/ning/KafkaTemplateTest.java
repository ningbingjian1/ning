package com.ning;

import org.jboss.netty.util.CharsetUtil;
import org.junit.Test;

/**
 * Created by ning on 2018/2/6.
 * User:ning
 * Date:2018/2/6
 * tIME:18:52
 */
public class KafkaTemplateTest {
    public void testProducer() throws Exception{
        com.ning.kafka.util.KafkaTemplate template = com.ning.kafka.util.KafkaTemplate.getInstance("19e");
        for(int i = 0 ;i < 10000;i ++){
            template.send("ning-behavior","a".getBytes(CharsetUtil.UTF_8),(i + "").getBytes(CharsetUtil.UTF_8));
        }
        Thread.sleep(10000);

    }
   @Test
    public void testConsumer(){

    }
}

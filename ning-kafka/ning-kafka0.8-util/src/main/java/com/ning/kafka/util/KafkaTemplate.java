package com.ning.kafka.util;


import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by ning on 2018/2/6.
 * User:ning
 * Date:2018/2/6
 * tIME:17:32
 */
public class KafkaTemplate {
    private static String DEFAULT_CONFIG_FILE = "/kafka/kafka.properties";
    private static Map<String,KafkaTemplate> mapping = new HashMap<>();
    private Properties config  ;
    private Producer<Object,Object> producer;
    private String topic ;
    private KafkaTemplate(){
    }
    /**
     *
     * @param configFile 配置文件
     * @param confPrefix kafka配置前缀
     * @return
     */
    public static KafkaTemplate getInstance(String configFile,String confPrefix){
        String key = configFile + "@" + confPrefix;
        KafkaTemplate template = mapping.get(key);
        if(template == null ){
            loadProperty(key,confPrefix);
        }
        return mapping.get(key);
    }

    private static void loadProperty(String confKey ,String confPrefix) {
        synchronized (KafkaTemplate.class){
            KafkaTemplate template = new KafkaTemplate();
            Properties  properties = new Properties();
            InputStream ips        = KafkaTemplate.class.getClassLoader().getResourceAsStream(confKey);
            try {
                properties.load(ips);
                Properties props = new Properties();
                Set<String> keys = properties.stringPropertyNames();
                for(String key : keys){
                    if(key.endsWith(".topic")){
                        template.topic = properties.getProperty(key);
                    } else {
                        props.setProperty(key.substring(confPrefix.length()),properties.getProperty(key));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(DEFAULT_CONFIG_FILE + " io exception ");
            }
            mapping.put(confKey,template);
        }
    }

    public static KafkaTemplate getInstance(){
        String key = DEFAULT_CONFIG_FILE;
        if(mapping.get(key) == null ){
            loadProperty(key,"");
        }
        return mapping.get(key);
    }
    public void send(String topic){
    }
    public Producer<Object,Object> getProducer(){
        return producer;
    }
    public <K,V> Producer<K, V> getNewProducer(){
        ProducerConfig                  producerConfig = new ProducerConfig(config);
        Producer<K,V> producer       = new Producer(producerConfig);
        return producer;
    }

}

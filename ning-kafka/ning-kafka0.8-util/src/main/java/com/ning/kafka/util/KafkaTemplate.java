package com.ning.kafka.util;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created by ning on 2018/2/6.
 * User:ning
 * Date:2018/2/6
 * tIME:17:32
 *
 *
 */
public class KafkaTemplate {
    private static String DEFAULT_CONFIG_FILE = "/kafka/kafka.properties";
    private static Map<String, KafkaTemplate> mapping = new HashMap<>();
    private Properties producerConfig;
    private Properties consumerConfig;
    private Properties kafkaConfig;
    private String topic;//对于producer.topic
    private Producer producer = null;
    private MultipleThreadConsumer multipleThreadConsumer ;
    private KafkaTemplate() {
    }

    /**
     * @param configFile 配置文件
     * @param confPrefix kafka配置前缀
     * @return
     */
    public static KafkaTemplate getInstance(String configFile, String confPrefix) {
        String key = configFile + "@" + confPrefix;
        KafkaTemplate template = mapping.get(key);
        if (template == null) {
            loadProperty(configFile, confPrefix);
        }
        return mapping.get(key);
    }

    /**
     *
     * @param file
     * @param confPrefix
     */
    private static void loadProperty(String file, String confPrefix) {
        synchronized (KafkaTemplate.class) {
            KafkaTemplate template = new KafkaTemplate();
            Properties properties = new Properties();
            InputStream ips = KafkaTemplate.class.getClassLoader().getParent().getResourceAsStream(file);
            try {
                if(ips == null ){
                    ips = KafkaTemplate.class.getResourceAsStream(file);
                }
                properties.load(ips);
                Properties kafkaProducerConfig = new Properties();
                Properties kafkaConsumerConfig = new Properties();
                Properties kafkaConfig = new Properties();
                String producerPrefix = confPrefix + ".producer.";
                String consumerPrefix = confPrefix + ".consumer.";
                String configPrefix = confPrefix + ".";
                Set<String> keys = properties.stringPropertyNames();
                Map<String,Integer> topicCountMap = new HashMap<>();
                for (String key : keys) {
                     if(key.startsWith(producerPrefix)) {
                         if (key.endsWith(".topic")) {
                             template.topic = properties.getProperty(key);
                         } else{
                             kafkaProducerConfig.setProperty(key.substring(producerPrefix.length()), properties.getProperty(key));
                         }
                    }else if(key.startsWith(consumerPrefix)){
                        if(key.endsWith(".topic")){
                            String topicThreadValue = properties.getProperty(key);
                            String [] topicsAndThreads = topicThreadValue.split(",");
                            for(String topicAndThreads : topicsAndThreads){
                                String []topicAndThread = topicAndThreads.split(":");
                                topicCountMap.put(topicAndThread[0],Integer.valueOf(topicAndThread[1]));
                            }
                        }else{
                            kafkaConsumerConfig.setProperty(key.substring(producerPrefix.length()), properties.getProperty(key));
                        }
                    }else{
                        kafkaConfig.setProperty(key.substring(configPrefix.length()), properties.getProperty(key));
                    }
                }
                template.producerConfig = kafkaProducerConfig ;
                template.consumerConfig = kafkaConsumerConfig ;
                template.kafkaConfig = kafkaConfig ;
                if(kafkaProducerConfig.size() > 0 ){
                    template.producer = new Producer(new ProducerConfig(kafkaProducerConfig));
                }
                if(kafkaConsumerConfig.size() > 0 ){
                    template.multipleThreadConsumer = new MultipleThreadConsumer(topicCountMap,kafkaConsumerConfig);
                }
            } catch (IOException e) {
                throw new RuntimeException(DEFAULT_CONFIG_FILE + " io exception ");
            }
            mapping.put(file + "@" + confPrefix, template);
        }
    }

    public static KafkaTemplate getInstance() {
        String key = DEFAULT_CONFIG_FILE;
        if (mapping.get(key) == null) {
            loadProperty(DEFAULT_CONFIG_FILE, "");
        }
        return mapping.get(key);
    }

    public static KafkaTemplate getInstance(String configPrefix) {
        String key = DEFAULT_CONFIG_FILE + "@" + configPrefix;
        if (mapping.get(key) == null) {
            loadProperty(DEFAULT_CONFIG_FILE, configPrefix);
        }
        return mapping.get(key);
    }
    public <K, V> void send(K k, V v) {
        KeyedMessage<K, V> keyedMessage = new KeyedMessage<>(topic, k, v);
        producer.send(keyedMessage);

    }
    public void startConsumer(BiConsumer<byte[], byte[]> recordProcessor){
        this.multipleThreadConsumer.start(recordProcessor);
    }

    public void stopConsumer(){
        this.multipleThreadConsumer.stop();
    }
    public void stopProducer(){
        this.producer.close();
    }
    public void stop(){
        try {
            this.producer.close();
            this.multipleThreadConsumer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

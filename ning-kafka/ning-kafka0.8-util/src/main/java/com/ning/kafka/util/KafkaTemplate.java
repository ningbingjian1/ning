package com.ning.kafka.util;


import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Created by ning on 2018/2/6.
 * User:ning
 * Date:2018/2/6
 * tIME:17:32
 */
public class KafkaTemplate {
    private static String DEFAULT_CONFIG_FILE = "/kafka/kafka.properties";
    private static Map<String, KafkaTemplate> mapping = new HashMap<>();
    private Properties producerConfig;
    private Properties consumerConfig;
    private Properties kafkaConfig;
    private String topic;
    Map<String, Producer> producerMap = new HashMap<>();

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
                for (String key : keys) {
                    if (key.endsWith(".topic")) {
                        template.topic = properties.getProperty(key);
                    } else if(key.startsWith(producerPrefix)) {
                        kafkaProducerConfig.setProperty(key.substring(producerPrefix.length()), properties.getProperty(key));
                    }else if(key.startsWith(consumerPrefix)){
                        if(key.endsWith(".topic")){
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

    public <K, V> void send(String topic, K k, V v) {
        String currentTopic = topic == null ?this.topic : topic ;
        Producer<K, V> producer = producerMap.get(currentTopic);
        if(producer == null ){
            synchronized (this){
                producer = new Producer<K, V>(new ProducerConfig(producerConfig));
                producerMap.put(topic,producer);
            }
        }
        KeyedMessage<K, V> keyedMessage = new KeyedMessage<>(topic, k, v);
        producer.send(keyedMessage);

    }
    public ExecutorService startConsumer(int numThreads, Map<String,Integer>topicAndCount, Consumer process){
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerConfig));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        return executor;
    }
}

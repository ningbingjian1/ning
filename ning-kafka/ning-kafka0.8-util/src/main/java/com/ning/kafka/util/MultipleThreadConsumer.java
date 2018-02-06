package com.ning.kafka.util;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class MultipleThreadConsumer {
    private ConsumerConnector consumer;
    private ExecutorService executor;
    private Map<String, Integer> topicCountMap;
    private Properties props;
    private  BiConsumer<byte[], byte[]>  recordProcessor;
    private volatile  boolean stop = true;

    /**
     * topic
     * @param topicCountMap 主题 消费线程数量
     * @param props
     */
    public MultipleThreadConsumer(Map<String, Integer> topicCountMap, Properties props) {
        this.topicCountMap = topicCountMap;
        this.props = props;
        this.recordProcessor = recordProcessor;
        Integer numThreads = topicCountMap.values().stream().max((a,b) ->{
            return a > b ? -1:(a < b ? 1 : 0);
        }).get();
        executor = Executors.newFixedThreadPool(numThreads);
    }
    public synchronized void start(BiConsumer<byte[], byte[]> recordProcessor) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        topicCountMap.forEach((topic,numThread) ->{
            List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
            for (final KafkaStream<byte[], byte[]> stream : streams) {
                executor.submit(() -> {
                    stream.forEach(msg -> {
                        recordProcessor.accept(msg.key(),msg.message());
                    });
                });
            }
        });
        stop = false;
    }


    public synchronized void stop() {
        if(!stop){
            if (consumer != null)
                consumer.shutdown();

            if (executor != null)
                executor.shutdown();
            stop = true ;
        }
    }
}

package com.ning.kafka.util;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.huan.analysis.entity.WatchRecord;
import tv.huan.analysis.util.ResultLiveLogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultipleThreadConsumer {
	private static Logger TCL_LOGGER = LoggerFactory.getLogger("TCL_LOG");
    private ConsumerConnector consumer;
    private ExecutorService executor;
    private String topic;
    private int numThreads;
    private Properties props;
    public MultipleThreadConsumer(String topic, int numThreads, Properties props) {
        this.numThreads = numThreads;
        this.topic = topic;
        this.props = props;
        executor = Executors.newFixedThreadPool(numThreads);
    }

    public void start() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(numThreads));
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.submit(() -> {
                stream.forEach(msg -> {
                	String line = new String(msg.message());
                    //final WatchRecord record = extractInfo(line);
                    //if (record != null && ChannelInfo.contains(record.channel_code)) {
                    	ResultLiveLogService.getInstance().saveResultLog(line);
                    //}
                });
            });
        }
    }


    private WatchRecord extractInfo(String msg) {
        String[] strArr = msg.split("\t");
        if (strArr.length == 11) {
            return new WatchRecord(strArr);
        }
        return null;
    }

    public void shutdown() {

        if (consumer != null)
            consumer.shutdown();

        if (executor != null)
            executor.shutdown();

    }
}

package com.ning.storm.first;

import com.ning.redis.ShardedJedisTemplate;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

// 定义一个Bolt，用于单词计数
public  class WordCount extends BaseBasicBolt {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		
		@Override
		public void execute(Tuple tuple, BasicOutputCollector collector){
			// 接收一个单词
			String word = tuple.getString(0);
			// 获取该单词对应的计数
			Integer count = counts.get(word);
			if(count == null)
				count = 0;
			// 计数增加
			count++;
			// 将单词和对应的计数加入map中
			counts.put(word,count);
			System.out.println(word +"	"+count);

			//ShardedJedisTemplate.getInstance().set(ShardedJedisTemplate.class.getSimpleName(),word +"	"+count);

			// 发送单词和计数（分别对应字段word和count）
			collector.emit(new Values(word, count));
		}
		
		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer){
			// 定义两个字段word和count
			declarer.declare(new Fields("word","count"));
		}
	}
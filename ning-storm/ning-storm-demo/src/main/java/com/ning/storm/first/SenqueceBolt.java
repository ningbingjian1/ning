package com.ning.storm.first;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class SenqueceBolt extends BaseBasicBolt {

    public void execute(Tuple arg0, BasicOutputCollector arg1) {
        String word = (String) arg0.getValue(0);
        String out = "Hello " + word + "!";
        //System.out.println(out);
        // 发送每一个单词
        String[] fields = out.split(" ");
        for(String field : fields){
            arg1.emit(new Values(field));
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        // 定义一个字段
        arg0.declare(new Fields("word"));
    }

}
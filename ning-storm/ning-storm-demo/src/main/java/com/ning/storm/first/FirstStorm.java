package com.ning.storm.first;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

public class FirstStorm {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new RandomSpout());
        builder.setBolt("bolt1", new SenqueceBolt()).shuffleGrouping("spout");
        // 设置slot——“count”,你并行度为12，它的数据来源是split的word字段
        builder.setBolt("count",new WordCount())
                .fieldsGrouping("bolt1", new Fields("word"));

        Config conf = new Config();
        conf.setDebug(false);
        if(args != null && args.length > 0) {
            conf.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (AlreadyAliveException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("firststorm", conf, builder.createTopology());
            Utils.sleep(30000);
            cluster.killTopology("firststorm");
            cluster.shutdown();
        }
    }

}
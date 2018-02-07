0. 查看分区情况
```
[analysis@kafka1 ~]$ /hwdata/kafka_2.10-0.8.2.2/bin/kafka-topics.sh --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181  --describe --topic tcl-live-data
Topic:ch-live-data	PartitionCount:4	ReplicationFactor:4	Configs:
	Topic: ch-live-data	Partition: 0	Leader: 1	Replicas: 1,2,3,4	Isr: 1,4,2,3
	Topic: ch-live-data	Partition: 1	Leader: 1	Replicas: 1,2,3,4	Isr: 1,4,2,3
	Topic: ch-live-data	Partition: 2	Leader: 1	Replicas: 1,2,3,4	Isr: 1,4,2,3
	Topic: ch-live-data	Partition: 3	Leader: 1	Replicas: 1,2,3,4	Isr: 1,4,2,3
```
发现有4个分区，每个分区4个副本，每个分区的leader都分配到1上去了，可见并没有起到负载均衡的作用
0.打开一个新的控制台订阅topic进行监控
```
/hwdata/kafka_2.10-0.8.2.2/bin/kafka-console-consumer.sh  --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181  --topic tcl-live-data 
```
1.生成tcl的json plan
1.1 生成tcl-live-data-topic-move.json,指定需要重新分配的topic ,/home/analysis/tmp/tcl/tcl-live-data-topic-move.json
```
{
	"topics": [{"topic": "tcl-live-data"}],
	 "version":1
}
```

1.2 执行命令 在控制台会打印副本分布的json plan
```
/hwdata/kafka_2.10-0.8.2.2/bin/kafka-reassign-partitions.sh --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181 --topics-to-move-json-file /home/analysis/tmp/tcl/tcl-live-data-topic-move.json --broker-list "1,2,3,4" --generate
```


2.编辑tcl的reassign json plan,调整合适的副本分布,副本在分区中的顺序要随机
vim /home/analysis/tmp/tcl/tcl-live-data-reassign-plan.json
```
{
    "version": 1, 
    "partitions": [
        {
            "topic": "tcl-live-data", 
            "partition": 0, 
            "replicas": [
                1, 
                2, 
                3, 
                4
            ]
        }, 
        {
            "topic": "tcl-live-data", 
            "partition": 1, 
            "replicas": [
                2, 
                1, 
                4, 
                3
            ]
        }, 
        {
            "topic": "tcl-live-data", 
            "partition": 2, 
            "replicas": [
                3, 
                4, 
                1, 
                2
            ]
        }, 
        {
            "topic": "tcl-live-data", 
            "partition": 3, 
            "replicas": [
                4, 
                3, 
                2, 
                1
            ]
        }
    ]
}
```

3.重新分配tcl的分区
/hwdata/kafka_2.10-0.8.2.2/bin/kafka-reassign-partitions.sh --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181  --reassignment-json-file /home/analysis/tmp/tcl/tcl-live-data-reassign-plan.json --execute

4.校验重分配是否完成
/hwdata/kafka_2.10-0.8.2.2/bin/kafka-reassign-partitions.sh --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181  --reassignment-json-file /home/analysis/tmp/tcl/tcl-live-data-reassign-plan.json --verify

4.继续检查topic的情况
/hwdata/kafka_2.10-0.8.2.2/bin/kafka-topics.sh --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181  --describe --topic tcl-live-data

5.编辑分区需要重新选举leader的topic和分区,编辑文件vim /home/analysis/tmp/tcl/tcl-topic-partition-list.json
 {
 "partitions":
  [
    {"topic": "tcl-live-data", "partition": 0},
    {"topic": "tcl-live-data", "partition": 1},
    {"topic": "tcl-live-data", "partition": 2},
	{"topic": "tcl-live-data", "partition": 3}
  ]
}
6.执行重新选举的命令 指定需要选举的topic 和分区都配置在文件tcl-topic-partition-list.json
/hwdata/kafka_2.10-0.8.2.2/bin/kafka-preferred-replica-election.sh --zookeeper 172.31.11.37:2181,172.31.11.38:2181,172.31.11.39:2181  --path-to-json-file /home/analysis/tmp/tcl/tcl-topic-partition-list.json

7.查看leader是否重新选举成功
```
[analysis@kafka1 ~]$ /hwdata/kafka_2.10-0.8.2.2/bin/kafka-topics.sh --zookeeper bigdataZookeeperEBL-704732236.cn-north-1.elb.amazonaws.com.cn:2181 --describe --topic tcl-live-data
Topic:tcl-live-data	PartitionCount:4	ReplicationFactor:4	Configs:
	Topic: tcl-live-data	Partition: 0	Leader: 1	Replicas: 1,2,3,4	Isr: 1,4,2,3
	Topic: tcl-live-data	Partition: 1	Leader: 2	Replicas: 2,1,4,3	Isr: 1,4,2,3
	Topic: tcl-live-data	Partition: 2	Leader: 3	Replicas: 3,4,1,2	Isr: 1,4,2,3
	Topic: tcl-live-data	Partition: 3	Leader: 4	Replicas: 4,3,2,1	Isr: 1,4,2,3
```
可见所有分区的leader都分布均匀了
 








 
 
 
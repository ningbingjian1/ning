package com.ning.es.demo.apitest;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Map;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/10
 * Time: 11:04
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class ClusterManagerApiTest {
    TransportClient client = null;
    IndicesAdminClient indicesAdminClient = null ;
    @Before
    public void setUp() throws Exception{
        //设置集群名称 和自动探测集群节点
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true)
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
    }
    @Test
    public void test(){
        ClusterHealthResponse res = client.admin().cluster().prepareHealth().get();
        System.out.println("dataOfNodes:" + res.getNumberOfDataNodes());
        System.out.println("nodes :" + res.getNumberOfNodes());
        for(Map.Entry<String,ClusterIndexHealth>  entry : res.getIndices().entrySet()){
            System.out.println(entry.getKey() + "-->" + entry.getValue().getNumberOfReplicas()+
                    "-->"+entry.getValue().getNumberOfShards() + "-->" + entry.getValue().getStatus());
        }
    }
    @Test
    public void tearDown(){
        client.close();
    }
}

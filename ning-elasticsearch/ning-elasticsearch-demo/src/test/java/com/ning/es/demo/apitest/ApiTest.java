package com.ning.es.demo.apitest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Random;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/3
 * Time: 10:21
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class ApiTest {
    Logger logger = LogManager.getLogger(this.getClass());
    TransportClient client = null;
    IndicesAdminClient indicesAdminClient = null ;
    @Before
    public void setUp()throws Exception{
        //设置集群名称 和自动探测集群节点
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true)
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
        indicesAdminClient = client.admin().indices();
    }
    //判断你是否存在
    @Test
    public void testExistIndex() throws Exception{
        String index = prepareIndex();
        deleteIndex(index);
        Assert.assertFalse("索引不应该存在",existsIndex(index));
    }
    @Test
    public void testCreateIndex()throws Exception{
        String index = prepareIndex();
        Assert.assertTrue("索引创建失败",createIndex(index));
        deleteIndex(index);
    }
    //删除索引
    @Test
    public void testDeleteIndex()throws Exception{
        String index = prepareIndex();
        Assert.assertTrue(createIndex(index));
        deleteIndex(index);
        Assert.assertFalse(existsIndex(index));
    }
    @Test
    public void testType(){

    }

    private String prepareIndex() {
        return "ning-" + new Random().nextInt(100);
    }

    //删除索引
    public boolean deleteIndex(String index) throws Exception{
        DeleteIndexRequest diq = new DeleteIndexRequest(index);
        return indicesAdminClient.delete(diq).get().isAcknowledged();
    }
    //创建索引
    public boolean createIndex(String index) throws Exception{
        CreateIndexRequest ciq = new CreateIndexRequest(index);
        CreateIndexResponse response = indicesAdminClient.create(ciq).get();
        return response.isAcknowledged();
    }
    public boolean existsIndex(String index){
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        DeleteIndexRequest diq = new DeleteIndexRequest(index);
        indicesAdminClient.delete(diq);
        return indicesAdminClient.prepareExists(index).get().isExists();
    }
    @Test
    public void testSettings(){
        indicesAdminClient.prepareCreate("");
    }
    @After
    public void tearDown()throws Exception{
        client.close();;
    }
}

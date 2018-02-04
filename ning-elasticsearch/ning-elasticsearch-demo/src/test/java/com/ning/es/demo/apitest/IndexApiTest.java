package com.ning.es.demo.apitest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/3
 * Time: 10:21
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class IndexApiTest {
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
        String index = prepareIndexName();
        deleteIndex(index);
        Assert.assertFalse("索引不应该存在",existsIndex(index));
    }
    @Test
    public void testCreateIndex()throws Exception{
        String index = prepareIndexName();
        Assert.assertTrue("索引创建失败",createIndex(index));
        deleteIndex(index);
    }
    //删除索引
    @Test
    public void testDeleteIndex()throws Exception{
        String index = prepareIndexName();
        Assert.assertTrue(createIndex(index));
        deleteIndex(index);
        Assert.assertFalse(existsIndex(index));
    }
    //测试是否存在type
    @Test
    public void testExistType() throws Exception{
        String index = prepareIndexName();
        createIndex(index);
        Assert.assertFalse(  indicesAdminClient.prepareTypesExists(index)
                .setTypes("type1","type2")
                .get().isExists());
        deleteIndex(index);

    }
    //测试配置
    @Test
    public void testCreateBySetting() throws Exception{
        String index = prepareIndexName();
        boolean ack = indicesAdminClient.prepareCreate(index)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards",3)
                        .put("index.number_of_replicas",3)).get().isAcknowledged();
        GetIndexRequest  giq = new GetIndexRequest().indices(index);
        Assert.assertTrue(ack);
        ImmutableOpenMap settings = indicesAdminClient.getIndex(giq).get().getSettings();
        Iterator<String> keys = settings.keysIt();
        while (keys.hasNext()){
            String next = keys.next();
            Settings settings1 = (Settings)settings.get(next);
            Map<String,String> map = settings1.getAsMap();
            for(Map.Entry<String,String> entry : map.entrySet()){
                String key = entry.getKey();
                if(key.equals("index.number_of_shards")){
                   Assert.assertTrue(Integer.valueOf(entry.getValue()) == 3 );
                   break;
                }
                //String printKey =  StringUtils.rightPad(key,30);
               // System.out.println(String.format("key = %s, value = %s",printKey,entry.getValue()));
            }

        }
        deleteIndex(index);

    }

    @Test
    public void testMapping() throws Exception{
        XContentBuilder builder =  XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject();
        String index = prepareIndexName();
        boolean ack = indicesAdminClient.prepareCreate(index).addMapping("statics",builder).get().isAcknowledged();
        Assert.assertTrue(ack);
        deleteIndex(index);
    }
    @Test
    public void testGetMapping() throws Exception{
        XContentBuilder builder =  XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject();
        String index = prepareIndexName();
        boolean ack = indicesAdminClient.prepareCreate(index).addMapping("statics",builder).get().isAcknowledged();
        Assert.assertTrue(ack);
        GetMappingsResponse response = indicesAdminClient.prepareGetMappings(index)
                .get();
        ImmutableOpenMap<String,MappingMetaData> mappings =  response.getMappings().get(index);
        MappingMetaData metaData = mappings.get("statics");
        Object obj = metaData.getSourceAsMap().get("properties");
        Object obj1 = ((Map<String,Object>)obj).get("name");
        String type = ((Map<String,Object>)obj1).get("type").toString();
        Assert.assertEquals("","keyword",type);
    }

    @Test
    public void testUpdateSetting() throws Exception{
        String index = prepareIndexName();
        boolean ack = indicesAdminClient.prepareCreate(index)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards",3)
                        .put("index.number_of_replicas",3)).get().isAcknowledged();
        Assert.assertTrue(ack);
        ack = indicesAdminClient.prepareUpdateSettings(index)
                .setSettings(Settings.builder().put("index.number_of_replicas",2)).get().isAcknowledged();
        Assert.assertTrue(ack);
        deleteIndex(index);
    }

    @Test
    public void testSomeApiTest() throws Exception{
        String index = prepareIndexName();
        String indexAliasName = index + "_alias";
        createIndex(index);
        indicesAdminClient.prepareRefresh(index).get();
        indicesAdminClient.prepareClose(index).get();
        indicesAdminClient.prepareOpen(index).get();
        indicesAdminClient.prepareAliases().addAlias(index,index + "_alias").get();
        GetAliasesResponse response = indicesAdminClient.prepareGetAliases(indexAliasName).get();
        System.out.println(response.getAliases().get(index).get(0).alias());
        deleteIndex(index);

    }

    private String prepareIndexName() {
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

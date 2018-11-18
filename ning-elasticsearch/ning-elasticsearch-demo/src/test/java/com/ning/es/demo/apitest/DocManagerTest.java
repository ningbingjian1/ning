package com.ning.es.demo.apitest;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/3
 * Time: 17:57
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class DocManagerTest {
    TransportClient client = null;
    IndicesAdminClient indicesAdminClient = null ;
    String index = "twitter";
    String type = "tweet";
    @Before
    public void setUp() throws Exception{
        //设置集群名称 和自动探测集群节点
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true)
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
        indicesAdminClient = client.admin().indices();
    }
    @Test
    public void insertDocByJson(){

        String source = "{\"user\":\"ning1\",\"post_date\":\"2017-01-01\",\"message\":\"this is a good message\"}";
        IndexResponse response  = client.prepareIndex(index,type,"1")
                .setSource(source).get();
       Assert.assertTrue(response.status() == RestStatus.CREATED);

    }
    @Test
    public void insertDocByMap(){
        Map<String,Object> doc = new HashMap<>();
        doc.put("user","ning2");
        doc.put("post_date","2017-01-02");
        doc.put("message","elasticsearch is so pretty");
        IndexResponse response = client.prepareIndex(index,type,"2")
                .setSource(doc)
                .get();
        Assert.assertTrue(response.status() == RestStatus.CREATED);

    }
    @Test
    public void insertDocByJsonBuilder() throws Exception{
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                .field("user","ning3")
                .field("post_date","2017-01-03")
                .field("message","fruit is so good")
                .endObject();
        IndexResponse response = client.prepareIndex(index,type,"3")
                .setSource(doc)
                .get();
        Assert.assertTrue(response.status() == RestStatus.CREATED);
    }
    @Test
    public void deleteDoc() throws Exception{
        String id = UUID.randomUUID().toString();
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                .field("user","ning3")
                .field("post_date","2017-01-03")
                .field("message","fruit is so good")
                .endObject();
        IndexResponse response = client.prepareIndex(index,type, id)
                .setSource(doc)
                .get();
       // System.out.println(response.status());
        DeleteResponse  dReponse = client.prepareDelete(index,type,id).get();
        Assert.assertTrue(dReponse.status() == RestStatus.OK);
    }
    @Test
    public void testUpdate() throws Exception{
        String id = UUID.randomUUID().toString();
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                .field("user","ning3")
                .field("post_date","2017-01-03")
                .field("message","fruit is so good")
                .endObject();
        IndexResponse response = client.prepareIndex(index,type, id)
                .setSource(doc)
                .get();
        XContentBuilder updateDoc = XContentFactory.jsonBuilder().startObject()
                .field("user","xxx")
                .field("post_date","2017-01-03")
                .field("message","fruit is so good")
                .endObject();
        UpdateRequest request = new UpdateRequest().index(index)
                .type(type)
                .id(id)
                .doc(updateDoc);
        UpdateResponse updateResponse = client.update(request).get();
        Assert.assertTrue(updateResponse.status() == RestStatus.OK);

    }
    //测试插入或者更新操作upsert
    @Test
    public void testUpsert() throws Exception{
        String id = UUID.randomUUID().toString();
        IndexRequest indexRequest = new IndexRequest(index,type,id)
                .source(XContentFactory.jsonBuilder().startObject()
                        .field("user","xxxy")
                        .field("post_date","2017-01-03")
                        .field("message","fruit is so good")
                        .endObject());
        XContentBuilder updateDoc = XContentFactory.jsonBuilder().startObject()
                .field("user","xxx")
                .field("post_date","2017-01-03")
                .field("message","fruit is so good")
                .endObject();
        UpdateRequest request = new UpdateRequest().index(index)
                .type(type)
                .id(id)
                .doc(updateDoc).upsert(indexRequest);
        client.update(request).actionGet();

    }
    //测试查询删除
    @Test
    public void testQueryDelete() throws Exception{
        String id = UUID.randomUUID().toString();
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                .field("user","ning4")
                .field("post_date","2017-01-03")
                .field("message","gooo")
                .endObject();
        IndexResponse indexResponse = client.prepareIndex(index,type,id)
                .setSource(doc)
                .get();
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("message","gooo"))
                .source(index)
                .get();
        long delete = response.getDeleted();
        System.out.println(delete);

    }

    //测试批量获取操作 GET
    @Test
    public void testMultiGet(){
        MultiGetResponse mResponse = client.prepareMultiGet()
                .add(index,type,"1")
                .add(index,type,"1","2","3","4")
                .add("another","type","foo")
                .get();
        for(MultiGetItemResponse itemResponse : mResponse){
            GetResponse getResponse = itemResponse.getResponse();
            if(getResponse != null && getResponse.isExists()){
                String json = getResponse.getSourceAsString();
                Assert.assertNotNull(json);
            }
        }

    }
    //测试多个操作一起进行 比如插入  更新  删除 一起进行
    @Test
    public void testBulk() throws Exception{
        BulkRequestBuilder builder = client.prepareBulk();
        //插入新文档
        String id1 = UUID.randomUUID().toString();
        XContentBuilder doc1 = XContentFactory.jsonBuilder().startObject()
                .field("user","ning" + id1)
                .field("post_date","2017-01-03")
                .field("message","ok you are very good people " + id1)
                .endObject();
        //新文档
        String id2 = UUID.randomUUID().toString();
        XContentBuilder doc2 = XContentFactory.jsonBuilder().startObject()
                .field("user","ning" + id2)
                .field("post_date","2017-01-03")
                .field("message","ok you are very good people " + id2)
                .endObject();

        String id3 = UUID.randomUUID().toString();
        XContentBuilder doc3 = XContentFactory.jsonBuilder().startObject()
                .field("user","ning" + id2)
                .field("post_date","2017-01-03")
                .field("message","ok you are very good people " + id3)
                .endObject();
        //将要更新的文档 更新id2
        XContentBuilder updateDoc = XContentFactory.jsonBuilder().startObject()
                .field("user","ning" + id2)
                .field("post_date","2017-01-03")
                .field("message","ok you are very good people " + id2)
                .endObject();

        IndexRequestBuilder irb1 = client.prepareIndex(index,type,id1)
                .setSource(doc1);
        IndexRequestBuilder irb2 = client.prepareIndex(index,type,id2)
                .setSource(doc2);
        IndexRequestBuilder irb3 = client.prepareIndex(index,type,id3)
                .setSource(doc3);
        UpdateRequestBuilder urb = client.prepareUpdate(index,type,id2).setDoc(updateDoc);
        DeleteRequestBuilder drb = client.prepareDelete(index,type,id3);
        builder.add(irb1).add(irb2).add(irb3).add(urb).add(drb).execute().actionGet();
    }

    @Test
    public void tearDown(){
        client.close();
    }
}

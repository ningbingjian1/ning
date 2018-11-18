package com.ning.es.demo.apitest;

import com.vividsolutions.jts.geom.Coordinate;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.exponentialDecayFunction;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;
import static org.elasticsearch.index.query.QueryBuilders.*;
/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/4
 * Time: 11:33
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 *
 *
 *
 *
 *
 *
 *
 $ curl -XPOST 'localhost:9200/books/IT/_bulk' --data-binary '@books.json'


 {"index":{ "_index": "books", "_type": "IT", "_id": "1" }}
 {"id":"1","title":"Java编程思想","language":"java","author":"Bruce Eckel","price":70.20,"publish_time":"2007-10-01","description":"Java学习必读经典,殿堂级著作！赢得了全球程序员的广泛赞誉。"}
 {"index":{ "_index": "books", "_type": "IT", "_id": "2" }}
 {"id":"2","title":"Java程序性能优化","language":"java","author":"葛一鸣","price":46.50,"publish_time":"2012-08-01","description":"让你的Java程序更快、更稳定。深入剖析软件设计层面、代码层面、JVM虚拟机层面的优化方法"}
 {"index":{ "_index": "books", "_type": "IT", "_id": "3" }}
 {"id":"3","title":"Python科学计算","language":"python","author":"张若愚","price":81.40,"publish_time":"2016-05-01","description":"零基础学python,光盘中作者独家整合开发winPython运行环境，涵盖了Python各个扩展库"}
 {"index":{ "_index": "books", "_type": "IT", "_id": "4" }}
 {"id":"4","title":"Python基础教程","language":"python","author":"Helant","price":54.50,"publish_time":"2014-03-01","description":"经典的Python入门教程，层次鲜明，结构严谨，内容翔实"}
 {"index":{ "_index": "books", "_type": "IT", "_id": "5" }}
 {"id":"5","title":"JavaScript高级程序设计","language":"javascript","author":"Nicholas C. Zakas","price":66.40,"publish_time":"2012-10-01","description":"JavaScript技术经典名著"}


 */
public class SearchApiTest {
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
    public void testQuery1() throws Exception{
        QueryBuilder builder = QueryBuilders.matchQuery("title","python").operator(Operator.AND);
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("title")
                .preTags("<span style=\"color:red\">")
                .postTags("</span>");
        SearchResponse response = client.prepareSearch("books")
                .setQuery(builder)
                .highlighter(highlightBuilder)
                .setSize(100)
                .get();
        SearchHits hits = response.getHits();
        for(SearchHit hit :hits){
            System.out.println("source = " + hit.getSourceAsString());
            System.out.println("source as map = " + hit.getSourceAsMap());
            System.out.println("index = " + hit.getIndex());
            System.out.println("type = " + hit.getType());
            System.out.println("id = " + hit.getId());
            System.out.println("price = " + hit.getSourceAsMap().get("price"));
            System.out.println("score = " + hit.getScore());
            Text []texts = hit.getHighlightFields().get("title").getFragments();
            if(texts != null ){
                for(Text text : texts){
                    System.out.println(text.toString());
                }
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }




    }


    //常用全文检索
    @Test
    public void testFullTextQuery(){
        QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        QueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("foo","hello world");
        QueryBuilder matchPhrasePrefixQuery = QueryBuilders.matchPhrasePrefixQuery("foo","hello w");
        QueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery("python","title","user");
        QueryBuilder commonTermQuery = QueryBuilders.commonTermsQuery("title","hello");
        QueryBuilder queryStringQuery = QueryBuilders.queryStringQuery("+kimcy -elasticsearch");
        QueryBuilder simpleQueryStringQuery = QueryBuilders.simpleQueryStringQuery("+kimcy -elasticsearch");

    }

    //常用基本查询
    @Test
    public void testTermQuery(){
        QueryBuilder termQuery = QueryBuilders.termQuery("title","java");
        termQuery = QueryBuilders.termsQuery("title","java","python");
        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("price")
                .from(100)
                .to(200)
                .includeLower(true)
                .includeUpper(false);
        QueryBuilder existQuery = QueryBuilders.existsQuery("language");
        QueryBuilder prefixQuery = QueryBuilders.prefixQuery("title","pyth");
        QueryBuilder wildcardQuery = QueryBuilders.wildcardQuery("author","张若?");
        QueryBuilder regexQuery = QueryBuilders.regexpQuery("title","bra.*");
        QueryBuilder fuzzQuery = QueryBuilders.fuzzyQuery("title","javascript");
        QueryBuilder typeQuery = QueryBuilders.typeQuery("IT");
        QueryBuilder idsQuery = QueryBuilders.idsQuery("3","5");




    }
    @Test
    public void testComplicateQuery(){

        //1.constantScoreQuery
        QueryBuilder constantScoreQuery = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("title","java"))
                .boost(2.0f);
        //2.disMaxQuery
        QueryBuilder disMaxQuery = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("title","java"))
                .add(QueryBuilders.termQuery("title","python"))
                .boost(2.1f)
                .tieBreaker(0.7f);

        //3.boolQuery
        QueryBuilder matchQuery1 = QueryBuilders.matchQuery("title","java");
        QueryBuilder matchQuery2 = QueryBuilders.matchQuery("description","虚拟机");
        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("price").gte(70);
        QueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(matchQuery1)
                .should(matchQuery2)
                .mustNot(rangeQuery);

        //4.indicesQuery
        QueryBuilder matchQuery3 = QueryBuilders.matchQuery("title","java");
        QueryBuilder matchQuery4 = QueryBuilders.matchQuery("description","虚拟机");
        QueryBuilder indicesQuery = QueryBuilders.indicesQuery(matchQuery3,"books","books2")
                .noMatchQuery(matchQuery4);

        //5.
        FunctionScoreQueryBuilder.FilterFunctionBuilder[]functions =
                {
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(matchQuery1,randomFunction("ABCDEF")),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(exponentialDecayFunction("age",0L,1L))

                };


        //6.boostingQuery
        QueryBuilder boostingQuery =
                QueryBuilders.boostingQuery(
                        matchQuery("title","java"),
                        rangeQuery("publish_time").lte("2017-01-01")
                ).negativeBoost(0.1f);
    }

    //嵌套查询
    @Test
    public void testNestedQuery(){
        //1.nestedQuery
        QueryBuilder nestedQuery = nestedQuery(
                "obj1",
                boolQuery()
                .must(matchQuery("obj1.name","blue"))
                .must(rangeQuery("obj1.count").gt(5))
                , ScoreMode.Avg
        );

        //2.hasChildQuery 1980年后出生的员工所在的分支机构
        //QueryBuilder hasChildQuery = QueryBuilder
    }

    @Test
    public void testGeoShapeQuery()throws Exception{
        Coordinate topLeft = new Coordinate(106.23248,38.48644);
        Coordinate bottomRight = new Coordinate(115.85794,38.48644);
        QueryBuilder geoShapeQuery = null ;
        QueryBuilders
                .geoShapeQuery("location", ShapeBuilders.newEnvelope(topLeft,bottomRight))
                .relation(ShapeRelation.WITHIN);

    }

    //矩形区域查询
    @Test
    public void testGeoBoundingBoxQuery(){
        GeoPoint topLeft = new GeoPoint(106.23248,38.48644);
        GeoPoint bottomRight = new GeoPoint(115.85794,38.48644);
        QueryBuilder geoBoundingBoxQuery = QueryBuilders.geoBoundingBoxQuery("location")
                .setCorners(topLeft,bottomRight);


    }

    //基于距离的查询
    @Test
    public void testDistinceQuery(){

        QueryBuilders.geoDistanceQuery("location")
                .point(38.48644,106.23248)
                .distance(200, DistanceUnit.KILOMETERS);
    }

    //多边形查询
    @Test
    public void testPolygonQuery(){
        GeoPoint topLeft = new GeoPoint(106.23248,38.48644);
        GeoPoint bottomRight = new GeoPoint(115.85794,38.48644);
        List<GeoPoint> points = Arrays.asList(topLeft,bottomRight);
        QueryBuilder query = QueryBuilders.geoPolygonQuery("location",points);
    }

    //特殊查询
    @Test
    public void testMoreLikeThis(){
        String []fields = {"title","description"};
        String []text = {"python"};

        MoreLikeThisQueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery(fields,text,null)
                .minTermFreq(1)
                .maxQueryTerms(12);

    }

    //脚本查询
    @Test
    public void testScriptQuery(){
        QueryBuilder builder = QueryBuilders.scriptQuery(new Script("doc['price'].value > 80"));
    }




    @After
    public void tearDown(){
        client.close();
    }
}

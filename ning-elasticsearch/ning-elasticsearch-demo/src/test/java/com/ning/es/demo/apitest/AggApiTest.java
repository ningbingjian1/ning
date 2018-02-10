package com.ning.es.demo.apitest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/2/4
 * Time: 17:29
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 聚合查询
 */
public class AggApiTest {
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
    public void testMaxAgg(){
        MaxAggregationBuilder maxAggregationBuilder =
                AggregationBuilders.max("agg")
                .field("price");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(maxAggregationBuilder)
                .get();
        Max max = sr.getAggregations().get("agg");
        double value = max.getValue();
        System.out.println("max = " + value);

    }

    @Test
    public void testMinAgg(){
        MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min("agg")
                .field("price");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(minAggregationBuilder)
                .get();
        Min min = sr.getAggregations().get("agg");
        System.out.println("min = " + min.value());
    }

    @Test
    public void testSumAgg(){
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("agg")
                .field("price");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(sumAggregationBuilder)
                .get();
        Sum sum = sr.getAggregations().get("agg");
        System.out.println("sum = " + sum.value());
    }
    @Test
    public void testAvg(){
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("agg")
                .field("price");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(avgAggregationBuilder)
                .get();
        Avg avg = sr.getAggregations().get("agg");
        System.out.println("avg = " + avg.value());

    }
    @Test
    public void testStats(){
        StatsAggregationBuilder statsAggregationBuilder =
                AggregationBuilders.stats("agg").field("price");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(statsAggregationBuilder)
                .execute().actionGet();
        Stats stats = sr.getAggregations().get("agg");
        System.out.println("count = " + stats.getCount());
        System.out.println("avg = " + stats.getAvg());
        System.out.println("sum = " + stats.getSum());
        System.out.println("min = " + stats.getMin());
        System.out.println("max = " + stats.getMax());
    }

    @Test
    public void testExtendStats(){
        ExtendedStatsAggregationBuilder esas = AggregationBuilders.extendedStats("agg").field("price");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(esas)
                .execute()
                .actionGet();
        ExtendedStats extendedStats = sr.getAggregations().get("agg");
        System.out.println("min = " + extendedStats.getMin());
        System.out.println("sum of square = " + extendedStats.getSumOfSquares());

    }
    @Test
    public void testValueCount(){
        ValueCountAggregationBuilder valueCountAggregationBuilder = AggregationBuilders.count("agg")
                .field("author");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(valueCountAggregationBuilder)
                .execute().actionGet();
        ValueCount valueCount = sr.getAggregations().get("agg");
        System.out.println(valueCount.getValue());
    }

    //桶聚合
    @Test
    public void testTermAgg(){
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("per_count")
                .field("language.keyword");
        SearchResponse sr = client.prepareSearch("books")
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = sr.getAggregations().get("per_count");
        for(Terms.Bucket entry : terms.getBuckets()){
            System.out.println("key = " + entry.getKey() + ",value = " + entry.getDocCount());
        }
    }
    @Test
    public void testFilterAgg(){
        FilterAggregationBuilder filterAgg = AggregationBuilders.filter("agg",
                QueryBuilders.termQuery("title","javascript"));
        SearchResponse res = client.prepareSearch("books").addAggregation(filterAgg).execute().actionGet();
        System.out.println(res.toString());
        Filter filter = res.getAggregations().get("agg");
        System.out.println(filter.getName() + ":" + filter.getDocCount());
    }
    @Test
    public void testFiltersAgg(){
        FiltersAggregationBuilder filterAgg = AggregationBuilders.filters("agg",
                QueryBuilders.termQuery("title","java"),
                QueryBuilders.termQuery("title","python"));
        SearchResponse res = client.prepareSearch("books").addAggregation(filterAgg).execute().actionGet();
        Filters filterAggResult = res.getAggregations().get("agg");
        System.out.println(res);
        for(Filters.Bucket bucket : filterAggResult.getBuckets()){
            String key = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            System.out.println(key + ":" + count);
        }
    }

    @Test
    public void testRangeAgg(){
        RangeAggregationBuilder rangeAgg = AggregationBuilders.range("agg")
                .field("price")
                .addRange(50,80)
                .addUnboundedTo(80)
                .addUnboundedFrom(50);
        SearchResponse res = client.prepareSearch("books")
                .addAggregation(rangeAgg)
                .execute()
                .actionGet();
        System.out.println(res);
        Range range = res.getAggregations().get("agg");
        for(Range.Bucket bucket : range.getBuckets()){
            System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount() +
                    ":from is " + bucket.getFrom() +
                    ":to is " + bucket.getTo()
            );
        }

    }

    @Test
    public void testDateRangeAgg(){
        AggregationBuilder dateAgg = AggregationBuilders.dateRange("agg")
                .field("publish_time")
                .format("yyyy-MM-dd")
                .addUnboundedTo("now-50M/M")
                .addUnboundedFrom("now-45M/M");
        SearchResponse res = client.prepareSearch("books").addAggregation(dateAgg).execute().actionGet();
        Range range = res.getAggregations().get("agg");
        for(Range.Bucket bucket : range.getBuckets()){
            System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount() +
                    ":from is " + bucket.getFrom() +
                    ":to is " + bucket.getTo()
            );
        }

    }

    @Test
    public void testDateHistogramAgg(){
        AggregationBuilder dateAgg = AggregationBuilders.dateHistogram("agg")
                .field("publish_time")
                .format("yyyy-MM-dd")
                .dateHistogramInterval(DateHistogramInterval.YEAR);

        SearchResponse res = client.prepareSearch("books").addAggregation(dateAgg).execute().actionGet();
        Histogram histogram = res.getAggregations().get("agg");
        for(Histogram.Bucket bucket : histogram.getBuckets()){
            System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount()
            );
        }
    }


    @Test
    public void tearDown(){
        client.close();
    }
}

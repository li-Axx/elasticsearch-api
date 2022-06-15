package com.zeekr.es.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zeekr.es.entity.EsPage;
import com.zeekr.es.entity.RosbagFragment;
import com.zeekr.es.mapper.RosbagFragmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * @author 59899
 */
@Component
@Slf4j
public class ElasticsearchUtil {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient rhlClient;
    private RestHighLevelClient client;
    @PostConstruct
    public void init() {
        client = this.rhlClient;
    }

    @Autowired
    private RosbagFragmentMapper repository;

    String err = "error is {}";
    String index = "test0614-1";

    /**
     * 检查索引是否存在
     * @param index 索引
     * @return boolean
     */
    public boolean existIndex(String index) {
        boolean exists = false;
        try {
            exists = client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
            if (exists) {
                log.info("{}, index in es", index);
            }else {
                log.info("{}, index not in es", index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(exists+ "");
        return exists;
    }

    /**
     * 添加索引
     * @param indexName 索引名称
     * @return CreateIndexResponse
     */
    public CreateIndexResponse createIndex(String indexName) {
        if (existIndex(indexName)) {
            return null;
        }else {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            CreateIndexResponse createIndexResponse = null;
            try {
                createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            } catch (IOException e) {
                log.info(err, e);
            }
            return createIndexResponse;
        }
    }

    /**
     * 分页+条件查询
     * @param fragment 查询条件体
     * @param pageSize 每页展示条数
     * @param pageNum 第 * 页
     * @return EsPage
     */
    public EsPage<RosbagFragment> selectFragmentForPage(RosbagFragment fragment, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "1") int pageNum) {
        long total = 0;
        if (!existIndex(index)){
            return new EsPage<>(pageNum, pageSize, total , null);
        }
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (ObjectUtils.isNotEmpty(fragment.getTags())) {
            fragment.getTags().forEach(tag -> {
                boolQueryBuilder.must(QueryBuilders.matchQuery("tags", tag));
            });
        }
        if (ObjectUtils.isNotEmpty(fragment.getCarId())) {
            // matchQuery() 精确查询(分词)
            boolQueryBuilder.must(QueryBuilders.matchQuery("carId", fragment.getCarId()));
        }
        if (ObjectUtils.isNotEmpty(fragment.getBeginTime())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("beginTime").gte(fragment.getBeginTime()));
        }
        if (ObjectUtils.isNotEmpty(fragment.getEndTime())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("endTime").lte(fragment.getEndTime()));
        }
        if (ObjectUtils.isNotEmpty(fragment.getOperator())) {
            String queryOperator = "*"+fragment.getOperator()+"*";
            // 模糊查询
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("operator", queryOperator));
        }
        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.from((pageNum - 1) * pageSize);
        sourceBuilder.size(pageSize);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(err, e);
            e.printStackTrace();
        }
        if (searchResponse != null) {
            SearchHit[] hits = searchResponse.getHits().getHits();
            List<RosbagFragment> fragments = new ArrayList<>();
            for (SearchHit hit : hits) {
                RosbagFragment fra = JSON.parseObject(hit.getSourceAsString(), RosbagFragment.class);
                fragments.add(fra);
            }
            total = searchResponse.getHits().getTotalHits().value;
            return new EsPage<>(pageNum, pageSize, total, fragments);
        } else {
            log.error("response is null");
            return new EsPage<>(pageNum, pageSize, total , null);
        }
    }

    /**
     * 添加文档
     * @param fragment 要添加的对象信息
     * @return IndexResponse
     */
    public IndexResponse createFragmentDoc(RosbagFragment fragment) {
//        String uuid = UUID.randomUUID().toString();
        IndexRequest request = new IndexRequest(index).id(fragment.getJobId()).source(JSON.toJSONString(fragment), XContentType.JSON);

        IndexResponse indexResponse = null;
        try {
            indexResponse = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(err, e);
        }
        return indexResponse;
    }

    /**
     * 根据文档id修改文档信息
     * @param fragment 需要修改的信息
     * @param uuid 文档id
     * @return UpdateResponse
     */
    public UpdateResponse updateFragmentDoc(RosbagFragment fragment, String uuid) {
        UpdateRequest updateRequest = new UpdateRequest(index, uuid);
        updateRequest.doc(JSON.toJSONString(fragment), XContentType.JSON);

        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(err, e);
        }
        return updateResponse;
    }


    /**
     * 根据文档id删除文档
     * @param uuid 文档id
     * @return DeleteResponse
     */
    public DeleteResponse deleteFragmentDoc(String uuid) {

        DeleteRequest deleteRequest = new DeleteRequest(index, uuid);

        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(err, e);
        }
        return deleteResponse;
    }


    /**
     * 根据文档id修改文档的标签
     * @param tags 标签集合
     * @param docUuid 文档id
     * @return UpdateResponse
     */
    public UpdateResponse updateFragmentTagsDoc(List<String> tags, String docUuid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tags", tags);
        UpdateRequest updateRequest = new UpdateRequest(index, docUuid);
        updateRequest.doc(map);

        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(err, e);
        }
        return updateResponse;
    }

    public List<RosbagFragment> selectAll() {
        if (!existIndex(index)){
            return null;
        }
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(err, e);
            e.printStackTrace();
        }
        if (searchResponse != null) {
            SearchHit[] hits = searchResponse.getHits().getHits();
            List<RosbagFragment> fragments = new ArrayList<>();
            for (SearchHit hit : hits) {
                RosbagFragment fra = JSON.parseObject(hit.getSourceAsString(), RosbagFragment.class);
                fragments.add(fra);
            }
            return fragments;
        } else {
            log.error("response is null");
            return null;
        }
    }

    public List<RosbagFragment> selectTest(int day) {
        if (!existIndex(index)){
            return null;
        }
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        /*String queryOperator = "*"+operator+"*";
        boolQueryBuilder.must(QueryBuilders.matchQuery("carId", carId));
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("operator", queryOperator));*/
//        boolQueryBuilder.must(QueryBuilders.termQuery("tags", tags));
//        tags.forEach(tag -> boolQueryBuilder.must(QueryBuilders.matchQuery("tags", tag)));
        /*Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022, 4-1, 30,8,0,0);
        Date beginDate = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2022, 5-1, 3,8,0,0);
        Date endDate = calendar2.getTime();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("beginTime").gte(beginDate));
        boolQueryBuilder.must(QueryBuilders.rangeQuery("endTime").lte(endDate));*/

        /*String name = "li";
        boolQueryBuilder.must(QueryBuilders.termQuery("operator", name));*/


        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(err, e);
            e.printStackTrace();
        }
        if (searchResponse != null) {
            SearchHit[] hits = searchResponse.getHits().getHits();
            List<RosbagFragment> fragments = new ArrayList<>();
            for (SearchHit hit : hits) {
                RosbagFragment fra = JSON.parseObject(hit.getSourceAsString(), RosbagFragment.class);
                fragments.add(fra);
            }
            log.info("data is {}", fragments);
            return fragments;
        } else {
            log.error("response is null");
            return null;
        }
    }

}
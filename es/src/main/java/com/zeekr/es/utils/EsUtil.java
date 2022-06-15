package com.zeekr.es.utils;


import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 59899
 */
@Component
public class EsUtil {

    @Qualifier("restHighLevelClient")
    @Autowired
    RestHighLevelClient client;

    /**
     * 创建索引
     * @param index 索引名称
     */
    public void creat(String index) {
        CreateIndexRequest request = new CreateIndexRequest(index);
        try {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(response);
        } catch (IOException e) {
            System.out.println("error is :"+e);
        }
    }

    public void exists(String index){
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            boolean exists = client.indices().exists(request,RequestOptions.DEFAULT);
            System.out.println(exists);
        } catch (IOException e) {
            System.out.println("error is :"+e);
        }
    }




}

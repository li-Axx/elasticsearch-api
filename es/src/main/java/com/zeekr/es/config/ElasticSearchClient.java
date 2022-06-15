package com.zeekr.es.config;


import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author 59899
 */
@Configuration
@Getter
@Setter
@ComponentScan(basePackageClasses = ElasticsearchClientFactory.class)
public class ElasticSearchClient {

/*    @Bean
    RestHighLevelClient getClient() {
        HttpHost httpHost = new HttpHost("127.0.0.1",9200);
        RestClientBuilder clientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);
        return client;
    }*/

    private Integer connectNum = 10;
    private Integer connectPerRoute = 50;

    private String hostName = "127.0.0.1";
    private int port = 9200;

    @Bean
    public HttpHost httpHost() {
        return new HttpHost(hostName, port);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public ElasticsearchClientFactory getFactory() {
        return ElasticsearchClientFactory.build(httpHost());
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient() {
        return getFactory().getClient();
    }

    @Bean(name = "restHighLevelClient")
    @Scope("singleton")
    public RestHighLevelClient getRHLClient() {
        return getFactory().getRhlClient();
    }

}

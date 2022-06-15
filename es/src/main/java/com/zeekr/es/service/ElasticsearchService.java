package com.zeekr.es.service;

import com.zeekr.es.entity.EsPage;
import com.zeekr.es.entity.RosbagFragment;
import org.elasticsearch.action.update.UpdateResponse;

import java.util.List;

/**
 * @author 59899
 */
public interface ElasticsearchService {

    void createIndex(String indexName);

    void createFragmentDoc(long id);

    EsPage<RosbagFragment> selectFragmentForPage(int pageSize, int pageNum);

    List<RosbagFragment> selectAll();

    List<RosbagFragment> selectTest(int day);

    UpdateResponse updateFragmentDoc(String uuid, long id);

}

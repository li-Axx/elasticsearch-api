package com.zeekr.es.controller;

import com.zeekr.es.entity.EsPage;
import com.zeekr.es.entity.RosbagFragment;
import com.zeekr.es.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 59899
 */
@RestController
@RequestMapping("lzp/test/es")
@Slf4j
public class EsController {

    @Autowired
    ElasticsearchService service;

    @PostMapping("create/index/{indexName}")
    public void createIndex(@PathVariable String indexName) {
        log.info("indexName is {}", indexName);
        service.createIndex(indexName);
    }

    @PostMapping("create/doc/fragment/{id}")
    public void createFragmentDoc(@PathVariable long id) {
        service.createFragmentDoc(id);
    }

    @PostMapping("select/doc/{pageSize}/{pageNum}")
    public EsPage<RosbagFragment> selectFragmentForPage(@PathVariable int pageSize, @PathVariable int pageNum) {
        return service.selectFragmentForPage(pageSize, pageNum);
    }

    @PostMapping("select/doc/all")
    public List<RosbagFragment> selectAll() {
        return service.selectAll();
    }

    @PostMapping("select/doc/test/{day}")
    public List<RosbagFragment> selectTest(@PathVariable int day) {
        return service.selectTest(day);
    }

    @PostMapping("update/doc/{uuid}/{id}")
    public UpdateResponse updateFragmentDoc(@PathVariable String uuid,@PathVariable long id){
        return service.updateFragmentDoc(uuid, id);
    }

}

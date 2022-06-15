package com.zeekr.es.service.impl;

import com.zeekr.es.entity.EsPage;
import com.zeekr.es.entity.RosbagFragment;
import com.zeekr.es.mapper.RosbagFragmentMapper;
import com.zeekr.es.service.ElasticsearchService;
import com.zeekr.es.utils.ElasticsearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 59899
 */
@Service
@Slf4j
@ComponentScan(basePackageClasses = RosbagFragmentMapper.class)
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private RosbagFragmentMapper mapper;
    @Autowired
    private ElasticsearchUtil esUtil;

    @Override
    public void createIndex(String indexName) {
        CreateIndexResponse index = esUtil.createIndex(indexName);
        log.info(index.toString());
    }

    @Override
    public void createFragmentDoc(long id) {
        RosbagFragment rosbagFragment = mapper.selectById(id);
        log.info(rosbagFragment.toString());

        IndexResponse fragmentDoc = esUtil.createFragmentDoc(rosbagFragment);
        log.info(fragmentDoc.toString());
    }

    @Override
    public EsPage<RosbagFragment> selectFragmentForPage(int pageSize, int pageNum) {
        RosbagFragment fra = new RosbagFragment();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022, 4-1, 30,8,0,0);
        Date beginDate = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2022, 5-1, 3,8,0,0);
        Date endDate = calendar2.getTime();
        List<String> tags = new ArrayList<>();

        tags.add("测试aaa");
        fra.setCarId("582");
        fra.setOperator("i");
        fra.setBeginTime(beginDate);
        fra.setEndTime(endDate);
        fra.setTags(tags);

        EsPage<RosbagFragment> page = esUtil.selectFragmentForPage(fra, pageSize, pageNum);
        log.info("data is {}", page.toString());
        return page;
    }

    @Override
    public List<RosbagFragment> selectAll() {
        return esUtil.selectAll();
    }

    @Override
    public List<RosbagFragment> selectTest(int day) {
        return esUtil.selectTest(day);
    }

    @Override
    public UpdateResponse updateFragmentDoc(String uuid, long id) {
        RosbagFragment rosbagFragment = mapper.selectById(id);
        return esUtil.updateFragmentDoc(rosbagFragment, uuid);
    }
}

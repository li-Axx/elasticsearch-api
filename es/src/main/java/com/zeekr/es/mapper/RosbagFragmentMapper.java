package com.zeekr.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeekr.es.entity.RosbagFragment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author 59899
 */
@Mapper
@Repository
public interface RosbagFragmentMapper extends BaseMapper<RosbagFragment> {
}

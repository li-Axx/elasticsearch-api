package com.zeekr.es.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: RosBagBase jsonExtend扩展字段
 * @Author: Jinyan.Zhang
 * @Date: 2022/03/15/下午8:41
 */
@Data
@Schema(description = "RosBagBase jsonExtend扩展字段")
public class ExtendJson {

    @Schema(description = "驾驶员")
    private String driver;

    @Schema(description = "车架号")
    private String carFrameNo;

    @Schema(description = "问题单名称")
    private String issueName;


}

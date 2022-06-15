package com.zeekr.es.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

import static com.zeekr.es.consts.DataManagerConstants.MS_TO_NS;

/**
 * @author dongfangwei
 * @description: TODO
 * @date 3/1/22 2:00 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RosbagBase extends BaseEntity {
    @Schema(description = "数据包名称")
    private String name;

    @Schema(description = "车牌号")
    private String carId;

    @Schema(description = "开始时间",example = "2021-12-22 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @Schema(description = "开始时间纳秒数",example = "0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long beginTimeNs;

    @Schema(description = "结束时间", example = "2021-12-22 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "结束时间纳秒数",example = "0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long endTimeNs;

    @Schema(description = "单包时长(纳秒)",example = "0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long bagDuration;

    @Schema(description = "操作人员")
    private String operator;

    @Schema(description = "存储路径")
    private String storePath;

    @Schema(description = "存储文件清单,以json数组的格式")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private BagStoreInfo storeInfo;

    @Schema(description = "topic列表,以json数组的格式")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> topics;

    @Schema(description = "扩展信息,json格式")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private ExtendJson jsonExtend;

    @Schema(description = "数据标签列表,以json数组格式")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> tags;

    @Schema(description = "任务ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "job_id")
    private String jobId;

    @Schema(description = "过期天数")
    @TableField(value = "expire_day")
    private Integer expireDay;

    @Schema(description = "版本号")
    private String version;

    public void autoFill(){
        //自动计算出beginTime和endTime的时间
        if (beginTime == null){
            beginTime =new Date(beginTimeNs/MS_TO_NS);
        }
        if (endTime == null){
            endTime = new Date(endTimeNs/MS_TO_NS);
        }
    }

    public void addTags(List<String> newTags){
        Set<String> tagSet = tags!=null?new HashSet<>(tags):new HashSet<>();
        tagSet.addAll(newTags);
        tags = new ArrayList<>(tagSet);
    }

    public void removeTags(List<String> delTags){
        Set<String> tagSet = tags!=null?new HashSet<>(tags):new HashSet<>();
        tagSet.removeAll(delTags);
        tags = new ArrayList<>(tagSet);
    }
}

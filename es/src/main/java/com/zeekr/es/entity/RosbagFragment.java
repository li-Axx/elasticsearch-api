package com.zeekr.es.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * 整合bagRaw和bagSlice两个表
 * @author 59899
 */
@Data
@TableName(value = "tbl_rosbag_fragment", autoResultMap = true)
public class RosbagFragment extends RosbagBase {

    @Schema(description = "片段的名称")
    private String type;

    @Schema(description = "原始数据集的ID")
    private Long pkgId;

    @Schema(description = "切分时间",example = "2021-12-22 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date splitTime;


    @Schema(description = "dmLabel列表,以json数组的格式")
    @TableField(value = "dm_labels", typeHandler = FastjsonTypeHandler.class)
    private List<String> dmLabels;

    @Schema(description = "片段信息描述")
    @TableField(value = "slice_describe")
    private String describe;

    public void addDmLabels(List<String> newDmLabels){
        Set<String> labelSet = dmLabels != null ? new HashSet<>(dmLabels) : new HashSet<>();
        labelSet.addAll(newDmLabels);
        dmLabels = new ArrayList<>(labelSet);
    }

    public void removeDmLabels(List<String> delDmLabels) {
        Set<String> labelSet = dmLabels != null ? new HashSet<>(dmLabels): new HashSet<>();
        labelSet.removeAll(delDmLabels);
        dmLabels = new ArrayList<>(labelSet);
    }

}

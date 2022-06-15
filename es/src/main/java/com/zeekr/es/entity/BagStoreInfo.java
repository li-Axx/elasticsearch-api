package com.zeekr.es.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dongfangwei
 * @description: Bag对应的文件清单, 包括rogbag包及其对应的回放数据
 * @date 1/12/22 11:25 AM
 */
@Data
public class BagStoreInfo {
    @Schema(description = "rosbag包的文件名")
    private String rosbag;
    @Schema(description = "各类回放文件的文件名,key:回放类别，value：文件名") // perception_replay 感知;fuse_replay 融合
    private Map<String, String> replay;
    @Schema(description = "预览文件名")
    private String thumbnail;
    @Schema(description = "封页数据")
    private String cover;
    private String info;     //SQLITE格式的明文信息(解包后)
    @Schema(description = "摄像头视频文件的文件名,key:摄像头类别，value：文件名")
    private Map<String, String> camera;
    @Schema(description = "Topic文件存储路径,key:topic名称，value:文件全路径")
    private Map<String, String> topics;
    @Schema(description = "其它类型的文件,key:文件类别，value：文件名")//key:healthy  质量分析
    private Map<String,String> others;

//    @Schema(description = "感知视频文件名, key:感知类别, value:文件路径")
//    private Map<String, String> perceive;


    //合并两个BagStoreInfo
    public void merge(BagStoreInfo target) {
        if (target == null) return;
        if (target.rosbag != null) this.rosbag = target.rosbag;
        this.replay = mergeMap(this.replay,target.replay);
        this.camera = mergeMap(this.camera,target.camera);
        if (target.thumbnail != null) this.thumbnail = target.thumbnail;
        if (target.info != null) this.info = target.info; // 后续可能不需要了
        this.topics = mergeMap(this.topics, target.topics);
        if (target.cover != null) this.cover = target.cover;
        this.others = mergeMap(this.others,target.others);
    }

    private <K,V> Map<K,V> mergeMap(Map<K,V> orgMap, Map<K,V> destMap){
        if (destMap == null) return orgMap;
        if (orgMap == null){
            orgMap = new HashMap<>();
        }
        orgMap.putAll(destMap);
        return orgMap;
    }
}

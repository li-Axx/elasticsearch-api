package com.zeekr.es.consts;

/**
 * @author dongfangwei
 * @description: 定义常量接口类
 * @date 12/28/21 9:06 AM
 */
public interface DataManagerConstants {
    Long MS_TO_NS = 1000000L;  //毫秒转纳秒
    Long SEC_TO_NS = MS_TO_NS * 1000L; //秒转纳秒

    String REPLAY_TYPE_RAW = "raw";         //原始数据回放
    String REPLAY_TYPE_SPLIT = "split";     //切片数据回放

    String KAFKA_TOPIC_SPIT_JOB_RESULT = "split-job-result-topic";        //切片任务处理结果消息
    String KAFKA_TOPIC_REPLAY_JOB_RESULT = "replay-job-result-topic";     //回放任务处理结果消息
    String KAFKA_ARGOS_JOB_STATUS_RESULT = "job-status-topic"; // 任务执行状态结果消息


    Integer RAW_DATA_PARSE_JOB_RUNNING = 1; // 原始数据解析任务状态-运行
    Integer RAW_DATA_PARSE_JOB_DONE = 2; // 原始数据解析任务状态-完成
    Integer RAW_DATA_PARSE_JOB_FAIL = 3; // 原始数据解析任务状态-失败


}

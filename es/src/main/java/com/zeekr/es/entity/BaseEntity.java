package com.zeekr.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 59899
 */
public class BaseEntity implements Serializable {
    @Schema(
            description = "主键id",
            example = "0"
    )
    @JsonFormat(
            shape = Shape.STRING
    )
    private Long id;
    @Schema(
            description = "数据创建时间",
            example = "2021-12-22 00:00:00"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTimeAt;
    @Schema(
            description = "数据最后更新时间",
            example = "2021-12-22 00:00:00"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateTimeAt;

    public BaseEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public Date getCreateTimeAt() {
        return this.createTimeAt;
    }

    public Date getUpdateTimeAt() {
        return this.updateTimeAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCreateTimeAt(final Date createTimeAt) {
        this.createTimeAt = createTimeAt;
    }

    public void setUpdateTimeAt(final Date updateTimeAt) {
        this.updateTimeAt = updateTimeAt;
    }
}

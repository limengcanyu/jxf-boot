package com.spring.boot.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author rock
 * @since 2023-02-15
 */
@NoArgsConstructor
@Data
@TableName("data_change_log")
public class DataChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String operationResult;

    private String operatorId;

    private String operatorName;

    private LocalDateTime createTime;

}

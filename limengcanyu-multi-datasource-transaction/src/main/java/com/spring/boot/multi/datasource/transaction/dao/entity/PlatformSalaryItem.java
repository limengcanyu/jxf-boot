package com.spring.boot.multi.datasource.transaction.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 薪资项定义表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformSalaryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    private String tenantId;

    /**
     * 薪资项编码
     */
    private String itemCode;

    /**
     * 薪资项名称
     */
    private String itemName;

    /**
     * 数据类型：1-文本；2-整数；3-小数
     */
    private Integer dataType;

    /**
     * 备注 
     */
    private String remark;

    /**
     * 是否启用：1-启用；0-禁用
     */
    private Boolean isActive;

    /**
     * 创建人
     */
    private String creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String modifierId;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 版本号
     */
    @Version
    private Integer version;


}

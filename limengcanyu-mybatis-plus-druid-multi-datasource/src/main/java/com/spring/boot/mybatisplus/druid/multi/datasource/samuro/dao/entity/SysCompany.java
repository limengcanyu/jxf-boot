package com.spring.boot.mybatisplus.druid.multi.datasource.samuro.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统公司表
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-07-24
 */
@Data
@Accessors(chain = true)
@TableName("sys_company")
public class SysCompany extends Model<SysCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    @TableId(value = "company_id", type = IdType.AUTO)
    private Integer companyId;
    /**
     * 公司编号
     */
    @TableField("company_number")
    private String companyNumber;
    /**
     * 中文名称
     */
    @TableField("chinese_name")
    private String chineseName;
    /**
     * 英文名称
     */
    @TableField("english_name")
    private String englishName;


    @Override
    protected Serializable pkVal() {
        return this.companyId;
    }

}

package com.spring.boot.flaw.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author rock.jiang
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExampleTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private String password;

    private String zhName;

    private Integer age;

    private String address;


}

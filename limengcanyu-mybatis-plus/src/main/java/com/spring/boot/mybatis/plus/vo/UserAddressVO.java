package com.spring.boot.mybatis.plus.vo;

import lombok.Data;

@Data
public class UserAddressVO {

    private Long uId;
    private String uName;
    private Integer uAge;
    private String uEmail;

    private Long aId;
    private Long aUserId;
    private String aName;

}

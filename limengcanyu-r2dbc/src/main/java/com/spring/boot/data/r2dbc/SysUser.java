package com.spring.boot.data.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SysUser {
    private int id;
    private String name;
    private int age;
    private String email;
}

package com.spring.boot.validation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Person {

    private String id;

    @NotNull(message = "名称不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Max(200)
    @Min(0)
    private Integer age;

}

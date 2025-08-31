package com.spring.boot.mybatis.plus.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spring.boot.mybatis.plus.mapper")
@SpringBootApplication
public class SpringBootMybatisPlusBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMybatisPlusBasicApplication.class, args);
	}

}

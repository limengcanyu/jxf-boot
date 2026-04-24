package com.spring.boot.httpcomponents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class SpringBootHttpcomponentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHttpcomponentsApplication.class, args);
	}

	/**
	 * http://localhost:8081/echo
	 *
	 * @return
	 */
	@RequestMapping("/echo")
	public String echo(){
		return "hello";
	}
}

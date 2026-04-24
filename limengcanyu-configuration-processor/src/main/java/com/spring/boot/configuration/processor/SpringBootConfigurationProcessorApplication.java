package com.spring.boot.configuration.processor;

import com.spring.boot.configuration.processor.config.ProcessorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class SpringBootConfigurationProcessorApplication {

	@Autowired
	private ProcessorConfig processorConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootConfigurationProcessorApplication.class, args);
	}

	@RequestMapping("/")
	public String index() {
		return "welcome here";
	}

	/**
	 * localhost:8080/getProcessorName
	 *
	 * @return
	 */
	@RequestMapping("/getProcessorName")
	public String getProcessorName() {
		return processorConfig.getName();
	}

}

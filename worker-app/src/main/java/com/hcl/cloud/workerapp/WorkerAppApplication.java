package com.hcl.cloud.workerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hcl.cloud.workerapp.config.RabbitmqConfig;



@EnableScheduling
@Import(RabbitmqConfig.class)
@RefreshScope
@SpringBootApplication
public class WorkerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkerAppApplication.class, args);
	}

}

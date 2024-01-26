package com.ecsfin.demo.zeebee;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@SpringBootApplication
public class DemoZeebeeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoZeebeeAppApplication.class, args);
	}

	
	@JobWorker(type="orchestrate-something")
	public Map<String, Object> doSomething(final ActivatedJob activatedJob){
		System.out.println("Received: "+activatedJob.getVariables());
		
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("resultValue1", 5);
		
		return resultMap;
	}
}

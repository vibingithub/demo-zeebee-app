package com.ecsfin.demo.zeebee.worker;

import java.util.Map;

import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class ShipmentWorker {

	@JobWorker(type = "item-shippment")
	public Map<String, Object> handle(final ActivatedJob job){
		System.out.println("Shipment Worker Started");
		
		Map<String, Object> properties = job.getVariablesAsMap();
		
		System.out.println("Shipment Worker Completed");
		
		return properties;
	}
}

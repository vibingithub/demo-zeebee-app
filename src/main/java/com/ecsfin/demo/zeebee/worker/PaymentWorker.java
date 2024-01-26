package com.ecsfin.demo.zeebee.worker;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecsfin.demo.zeebee.model.Item;
import com.ecsfin.demo.zeebee.processor.PaymentProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class PaymentWorker {
	@Autowired
	ZeebeClient zeebeClient;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@JobWorker(type = "item-payment")
	public Map<String, Object> handle(final ActivatedJob job){
		System.out.println("Payment Worker Started");
		
		Map<String, Object> properties = job.getVariablesAsMap();
		
		//Thread thread = new PaymentProcessor(properties, zeebeClient, objectMapper);
		//thread.start();
		
		try {
			String itemStr = objectMapper.writeValueAsString(properties.get("item"));
			Item item = objectMapper.readValue(itemStr, Item.class);
			
			int total = item.getQuantity()*item.getPrice();
			item.setAmount(BigDecimal.valueOf(total));
			
			properties.put("item", item);
			properties.put("ItemPaymentCorrKey", item.getItemId());
			properties.put("ItemPaymentFailedCorrId", "");
			
			zeebeClient.newPublishMessageCommand()
					.messageName("ItemPaymentSuccessEvent")
					.correlationKey(item.getItemId())
					.variables(properties)
					.send().join();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Payment Worker Completed");
		return properties;
	}
}

package com.ecsfin.demo.zeebee.processor;

import java.math.BigDecimal;
import java.util.Map;

import com.ecsfin.demo.zeebee.model.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.ZeebeClient;

public class PaymentProcessor extends Thread{
	
	private Map<String, Object> properties;
	private ZeebeClient zeebeClient;
	private ObjectMapper objectMapper;
	
	public PaymentProcessor(Map<String, Object> properties, ZeebeClient zeebeClient, ObjectMapper objectMapper) {
		this.properties = properties;
		this.zeebeClient = zeebeClient;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void run() {
		System.out.println("PaymentProcessor Started");
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
		System.out.println("PaymentProcessor Completed");
	}

}

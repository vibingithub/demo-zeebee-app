package com.ecsfin.demo.zeebee.worker;

import java.util.Map;
import java.util.UUID;
import java.util.random.RandomGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecsfin.demo.zeebee.model.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class SelectItemWorker {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ZeebeClient zeebeClient;
	
	@JobWorker(type = "select-item")
	public Map<String, Object> handle(final ActivatedJob job){
		System.out.println("Select Item Worker Started");
		
		Map<String, Object> properties = job.getVariablesAsMap();
		
		try {
			String itemStr = objectMapper.writeValueAsString(properties.get("item"));
			Item item = objectMapper.readValue(itemStr, Item.class);
			
			int randomInt = RandomGenerator.getDefault().nextInt(9);
			item.setQuantity(randomInt);
			
			properties.put("item", item);
			properties.put("ItemSelectedCorrKey", item.getItemId());
			properties.put("ItemQuantityCorrKey", "");
			properties.put("quantity",item.getQuantity());
			
			zeebeClient.newPublishMessageCommand()
					.messageName("ItemSelectedSucessEvent")
					.correlationKey(item.getItemId())
					.variables(properties)
					.send().join();
			
			System.out.println("Select Item Worker Completed");
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
}

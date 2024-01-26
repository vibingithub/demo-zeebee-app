package com.ecsfin.demo.zeebee.service;

import java.util.Map;
import java.util.UUID;
import java.util.random.RandomGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecsfin.demo.zeebee.model.Item;

import io.camunda.zeebe.client.ZeebeClient;

@Service
public class ShoppingServiceImpl implements ShoppingService {
	@Autowired
	ZeebeClient zeebeClient;

	@Override
	public void initiateShopping(Item item) {
		int randomInt = RandomGenerator.getDefault().nextInt(9);
		
		item.setItemId(UUID.randomUUID().toString());
		
		if(randomInt%2==0) {
			item.setQuantity(randomInt);
		}else {
			item.setQuantity(0);
		}
		
		Map<String, Object> dataVar = Map.of("itemId", item.getItemId(),
											"item", item,
											"quantity",item.getQuantity());
		zeebeClient.newCreateInstanceCommand()
				.bpmnProcessId("online-shopping-flow")
				.latestVersion()
				.variables(dataVar)
				.withResult()
				.send();
		System.out.println("Process Started");
	}

}

package com.ecsfin.demo.zeebee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecsfin.demo.zeebee.model.Item;
import com.ecsfin.demo.zeebee.service.ShoppingService;

@RestController
@RequestMapping("/v1/shopping")
public class DemoController {
	@Autowired
	ShoppingService shoppingService;

	@PostMapping
	public ResponseEntity<String> initiateOnlineShopping(@RequestBody Item item) {
		shoppingService.initiateShopping(item);
		return new ResponseEntity<String>("Inititated", HttpStatus.ACCEPTED);
	}
}

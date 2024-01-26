package com.ecsfin.demo.zeebee.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Item {
	
	private String itemId;
	private String name;
	private int price;
	private int quantity;
	private BigDecimal amount;
}

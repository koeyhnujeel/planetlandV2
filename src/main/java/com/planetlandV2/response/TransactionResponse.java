package com.planetlandV2.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TransactionResponse {

	private String buyer;
	private String seller;
	private String planet;
	private Integer price;
	private LocalDateTime TransactionDate;

	@Builder
	public TransactionResponse(String buyer, String seller, String planet, Integer price,
		LocalDateTime transactionDate) {
		this.buyer = buyer;
		this.seller = seller;
		this.planet = planet;
		this.price = price;
		TransactionDate = transactionDate;
	}
}

package com.planetlandV2.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeResponse {
	private String planetName;
	private Integer price;
	private String message;

	@Builder
	public TradeResponse(String planetName, Integer price) {
		this.planetName = planetName;
		this.price = price;
		this.message = "구매가 완료되었습니다.";
	}
}

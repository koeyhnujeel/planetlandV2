package com.planetlandV2.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanetSell {

	@Min(value = 1, message = "The minimum price is 1 won.")
	@NotNull(message = "Please enter the price.")
	private Integer sellPrice;

	@Builder
	public PlanetSell(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}
}

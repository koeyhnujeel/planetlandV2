package com.planetlandV2.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanetSell {

	@Min(value = 1, message = "행성 최소 가격은 1원입니다.")
	@NotNull(message = "판매 가격을 입력해주세요.")
	private Integer sellPrice;

	@Builder
	public PlanetSell(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}
}

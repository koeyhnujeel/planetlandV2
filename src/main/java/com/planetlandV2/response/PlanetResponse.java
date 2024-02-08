package com.planetlandV2.response;

import com.planetlandV2.Enum.PlanetStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlanetResponse {

	private Long id;

	private String planetName;

	private Integer price;

	private PlanetStatus planetStatus;

	@Builder
	public PlanetResponse(Long id, String planetName, Integer price, PlanetStatus planetStatus) {
		this.id = id;
		this.planetName = planetName;
		this.price = price;
		this.planetStatus = planetStatus;
	}
}

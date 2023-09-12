package com.planetlandV2.requset;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanetEdit {

	private String planetName;

	private int price;

	private int population;

	private int satellite;

	private String planetStatus;

	@Builder
	public PlanetEdit(String planetName, int price, int population, int satellite, String planetStatus) {
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
	}
}

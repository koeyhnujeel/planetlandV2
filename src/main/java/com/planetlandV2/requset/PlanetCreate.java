package com.planetlandV2.requset;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlanetCreate {

	private String planetName;

	private int price;

	private int population;

	private int satellite;

	private String planetStatus;

	@Builder
	public PlanetCreate(String planetName, int price, int population, int satellite, String planetStatus) {
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
	}
}

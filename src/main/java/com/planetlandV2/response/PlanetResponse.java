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

	private Integer population;

	private Integer satellite;

	private PlanetStatus planetStatus;

	private String owner;

	private String imgName;

	private String imgPath;


	@Builder
	public PlanetResponse(Long id, String planetName, Integer price, Integer population, Integer satellite,
		PlanetStatus planetStatus, String owner, String imgName, String imgPath) {
		this.id = id;
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
		this.owner = owner;
		this.imgName = imgName;
		this.imgPath = imgPath;
	}
}

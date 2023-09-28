package com.planetlandV2.response;

import com.planetlandV2.domain.Planet;

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

	private String planetStatus;

	private String imgName;

	private String imgPath;

	public PlanetResponse(Planet planet) {
		this.id = planet.getPlanetId();
		this.planetName = planet.getPlanetName();
		this.price = planet.getPrice();
		this.population = planet.getPopulation();
		this.satellite = planet.getSatellite();
		this.planetStatus = planet.getPlanetStatus();
		this.imgName = planet.getImgName();
		this.imgPath = planet.getImgPath();
	}

	@Builder
	public PlanetResponse(Long id, String planetName, Integer price, Integer population, Integer satellite,
		String planetStatus,
		String imgName, String imgPath) {
		this.id = id;
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
		this.imgName = imgName;
		this.imgPath = imgPath;
	}
}
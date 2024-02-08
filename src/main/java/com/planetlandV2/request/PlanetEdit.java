package com.planetlandV2.request;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.exception.planet.InvalidRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class PlanetEdit {

	private String planetName;

	private Integer price;

	private Integer population;

	private Integer satellite;

	private PlanetStatus planetStatus;

	@Builder
	public PlanetEdit(String planetName, Integer price, Integer population, Integer satellite, PlanetStatus planetStatus) {
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
	}

	public void validate() {
		if (this.planetName.contains("바보")) {
			throw new InvalidRequest("planetName", "행성 이름에 비속어는 포함할 수 없습니다.");
		}
	}
}

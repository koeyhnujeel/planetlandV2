package com.planetlandV2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.planetlandV2.requset.PlanetEdit;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Planet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long planetId;

	private String planetName;

	private int price;

	private int population;

	private int satellite;

	private String planetStatus;

	private String imgName;

	private String imgPath;

	@Builder
	public Planet(Long planetId, String planetName, int price, int population, int satellite, String planetStatus,
		String imgName, String imgPath) {
		this.planetId = planetId;
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
		this.imgName = imgName;
		this.imgPath = imgPath;
	}

	public void edit(PlanetEdit planetEdit) {
		this.planetName = planetEdit.getPlanetName();
		this.price = planetEdit.getPrice();
		this.population = planetEdit.getPopulation();
		this.satellite = planetEdit.getSatellite();
		this.planetStatus = planetEdit.getPlanetStatus();
	}

	public void imgEdit(String imgName, String imgPath) {
		this.imgName = imgName;
		this.imgPath = imgPath;
	}
}

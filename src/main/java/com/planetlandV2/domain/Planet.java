package com.planetlandV2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.requset.PlanetEdit;
import com.planetlandV2.requset.PlanetSell;
import com.planetlandV2.response.PlanetResponse;

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

	private Integer price;

	private Integer population;

	private Integer satellite;

	private PlanetStatus planetStatus;

	private String owner;

	private String imgName;

	private String imgPath;

	@ManyToOne
	private User user;

	@Builder
	public Planet(Long planetId, String planetName, Integer price, Integer population, Integer satellite, PlanetStatus planetStatus,
		String owner, String imgName, String imgPath) {
		this.planetId = planetId;
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
		this.owner = owner;
		this.imgName = imgName;
		this.imgPath = imgPath;
	}

	public PlanetResponse toResponse() {
		return PlanetResponse.builder()
			.id(this.planetId)
			.planetName(this.planetName)
			.price(this.price)
			.population(this.population)
			.satellite(this.satellite)
			.planetStatus(this.planetStatus)
			.owner(this.owner)
			.imgName(this.imgName)
			.imgPath(this.imgPath)
			.build();
	}

	public void edit(PlanetEdit planetEdit) {
		this.planetName = planetEdit.getPlanetName();
		this.price = planetEdit.getPrice();
		this.population = planetEdit.getPopulation();
		this.satellite = planetEdit.getSatellite();
		this.planetStatus = planetEdit.getPlanetStatus();
		this.owner = planetEdit.getOwner();
	}

	public void imgEdit(String imgName, String imgPath) {
		this.imgName = imgName;
		this.imgPath = imgPath;
	}

	public void editPriceAndStatus(PlanetSell planetSell) {
		this.price = planetSell.getSellPrice();
		this.planetStatus = PlanetStatus.FORSALE;
	}

	public void changeOwnerAndStatus(User buyer) {
		this.owner = buyer.getNickname();
		this.planetStatus = PlanetStatus.NOTFORSALE;
		this.user = buyer;
	}
}

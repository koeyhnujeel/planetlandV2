package com.planetlandV2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.request.PlanetEdit;
import com.planetlandV2.request.PlanetSell;
import com.planetlandV2.response.PlanetDetailResponse;
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

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Builder
	public Planet(String planetName, Integer price, Integer population, Integer satellite, PlanetStatus planetStatus,
		String owner, String imgName, String imgPath) {
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
			.planetStatus(this.planetStatus)
			.build();
	}

	public PlanetDetailResponse toDetailResponse() {
		return PlanetDetailResponse.builder()
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
		this.planetName = (planetEdit.getPlanetName().isEmpty()) ? this.planetName : planetEdit.getPlanetName();
		this.price = (planetEdit.getPrice() == null) ? this.price : planetEdit.getPrice();
		this.population = (planetEdit.getPopulation() == null) ? this.population : planetEdit.getPopulation();
		this.satellite = (planetEdit.getSatellite() == null) ? this.satellite : planetEdit.getSatellite();
		this.planetStatus = (planetEdit.getPlanetStatus() == null) ? this.planetStatus : planetEdit.getPlanetStatus();
	}

	public void imgEdit(String imgName, String imgPath) {
		this.imgName = imgName;
		this.imgPath = imgPath;
	}

	public void salesSetting(PlanetSell planetSell) {
		this.price = planetSell.getSellPrice();
		this.planetStatus = PlanetStatus.FORSALE;
	}

	public void changeOwnerAndStatus(User buyer) {
		this.owner = buyer.getNickname();
		this.planetStatus = PlanetStatus.NOTFORSALE;
		this.user = buyer;
	}

	public void cancelSale(PlanetStatus planetStatus) {
		this.planetStatus = planetStatus;
	}
}

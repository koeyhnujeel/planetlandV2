package com.planetlandV2.request;

import static com.planetlandV2.image.ImageProcess.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.exception.planet.InvalidRequest;

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

	@NotBlank(message = "행성 이름을 입력해주세요.")
	private String planetName;

	@Min(value = 1, message = "행성 최소 가격은 1원입니다.")
	@NotNull(message = "행성 가격을 입력해주세요.")
	private Integer price;

	@NotNull(message = "행성 인구수를 입력해주세요.")
	private Integer population;

	@NotNull(message = "위성수를 입력해주세요.")
	private Integer satellite;

	@NotNull(message = "판매 여부를 입력해주세요.")
	private PlanetStatus planetStatus;

	@Builder
	public PlanetCreate(String planetName, Integer price, Integer population, Integer satellite, PlanetStatus planetStatus) {
		this.planetName = planetName;
		this.price = price;
		this.population = population;
		this.satellite = satellite;
		this.planetStatus = planetStatus;
	}

	public Planet toEntity(String imgName) {
		return Planet.builder()
			.planetName(this.planetName)
			.price(this.price)
			.population(this.population)
			.satellite(this.satellite)
			.planetStatus(this.planetStatus)
			.owner("ADMIN")
			.imgName(imgName)
			.imgPath(PATH + imgName)
			.build();
	}

	public void validate() {
		if (this.planetName.contains("바보")) {
			throw new InvalidRequest("planetName", "행성 이름에 비속어는 포함할 수 없습니다.");
		}
	}
}

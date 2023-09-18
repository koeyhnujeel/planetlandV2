package com.planetlandV2.requset;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.planetlandV2.exception.InvalidRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanetEdit {

	@NotBlank(message = "행성 이름을 입력해주세요.")
	private String planetName;

	@Min(value = 1, message = "행성 최소 가격은 1원입니다.")
	@NotNull(message = "행성 가격을 입력해주세요.")
	private Integer price;

	@NotNull(message = "행성 인구수를 입력해주세요.")
	private Integer population;

	@NotNull(message = "위성수를 입력해주세요.")
	private Integer satellite;

	@NotBlank(message = "구매가능 여부를 입력해주세요.")
	private String planetStatus;

	@Builder
	public PlanetEdit(String planetName, Integer price, Integer population, Integer satellite, String planetStatus) {
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

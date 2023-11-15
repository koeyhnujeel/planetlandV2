package com.planetlandV2.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PlanetStatus { //todo @JsonCreator, @JsonValue 정리
	FORSALE("판매"),
	NOTFORSALE("미판매");

	private final String value;

	PlanetStatus(String value) {
		this.value = value;
	}

	@JsonCreator
	public static PlanetStatus from(String value) {
		for (PlanetStatus status : PlanetStatus.values()) {
			if (status.getValue().equals(value)) {
				return status;
			}
		}
		return null;
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}

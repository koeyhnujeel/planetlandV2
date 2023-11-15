package com.planetlandV2.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

public enum TradeType {
	BUY("구매"),
	SELL("판매");

	private final String value;

	TradeType(String value) {
		this.value = value;
	}

	@JsonCreator
	public static TradeType from(String value) {
		for (TradeType type : TradeType.values()) {
			if (type.getValue().equals(value)) {
				return type;
			}
		}
		return null;
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}

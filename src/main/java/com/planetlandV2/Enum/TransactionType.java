package com.planetlandV2.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {
	ALL("전체"),
	BUY("구매"),
	SELL("판매");


	private final String value;

	TransactionType(String value) {
		this.value = value;
	}

	@JsonCreator
	public static TransactionType from(String value) {
		for (TransactionType type : TransactionType.values()) {
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

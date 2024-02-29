package com.planetlandV2.Enum;
public enum TransactionType {
	ALL("전체"),
	BUY("구매"),
	SELL("판매");


	private final String value;

	TransactionType(String value) {
		this.value = value;
	}

	public static TransactionType from(String value) {
		for (TransactionType type : TransactionType.values()) {
			if (type.getValue().equals(value)) {
				return type;
			}
		}
		return null;
	}

	public String getValue() {
		return value;
	}
}

package com.planetlandV2.requset;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlanetPage {

	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 10;

	@Builder.Default
	private String keyword = "최신순";

	public long getOffset() {
		return (long) (max(1, page) - 1) * max(10, size);
	}

	public boolean isAscending() {
		return !keyword.equals("최신순") && !keyword.equals("높은 가치순");
	}

	public String getProperty() {
		if (keyword.equals("최신순") || keyword.equals("과거순")) {
			return "planetId";
		}
		return "price";
	}
}

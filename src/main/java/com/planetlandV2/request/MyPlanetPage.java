package com.planetlandV2.request;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPlanetPage {

	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 10;

	public long getOffset() {
		return (long) (max(1, page) - 1) * max(10, size);
	}
}

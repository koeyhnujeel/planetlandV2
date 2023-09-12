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

	public long getOffset() {
		return (long) (max(1, page) - 1) * max(10, size);
	}
}

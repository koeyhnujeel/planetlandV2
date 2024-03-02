package com.planetlandV2.Reader;

import com.planetlandV2.domain.Planet;

public interface PlanetReader {

	Planet read(Long planetId);

	Planet readForBuy(Long planetId);
}

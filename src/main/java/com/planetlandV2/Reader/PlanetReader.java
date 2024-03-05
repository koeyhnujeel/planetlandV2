package com.planetlandV2.Reader;

import java.util.List;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.request.PlanetPage;
import com.planetlandV2.response.PlanetResponse;

public interface PlanetReader {

	Planet read(Long planetId);

	List<PlanetResponse> readList(PlanetPage planetPage);

	Planet readForBuy(Long planetId);
}

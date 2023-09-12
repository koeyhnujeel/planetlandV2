package com.planetlandV2.repository;

import java.util.List;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.requset.PlanetPage;

public interface PlanetRepositoryCustom {

	List<Planet> getList(PlanetPage planetPage);
}

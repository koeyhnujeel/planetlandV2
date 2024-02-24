package com.planetlandV2.repository;

import java.util.List;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.request.MyPlanetPage;
import com.planetlandV2.request.PlanetPage;

public interface PlanetRepositoryCustom {

	List<Planet> getList(PlanetPage planetPage);

	List<Planet> getMyPlanetList(MyPlanetPage myPlanetPage);
}

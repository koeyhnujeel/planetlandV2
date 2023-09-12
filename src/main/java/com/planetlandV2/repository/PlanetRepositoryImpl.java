package com.planetlandV2.repository;

import java.util.List;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.QPlanet;
import com.planetlandV2.requset.PlanetPage;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlanetRepositoryImpl implements PlanetRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Planet> getList(PlanetPage planetPage) {
		return jpaQueryFactory.selectFrom(QPlanet.planet)
			.limit(planetPage.getSize())
			.offset(planetPage.getOffset())
			.orderBy(QPlanet.planet.planetId.desc())
			.fetch();
	}
}

package com.planetlandV2.repository;

import java.util.List;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.QPlanet;
import com.planetlandV2.requset.PlanetPage;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlanetRepositoryCustomImpl implements PlanetRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Planet> getList(PlanetPage planetPage) {
		return jpaQueryFactory.selectFrom(QPlanet.planet)
			.limit(planetPage.getSize())
			.offset(planetPage.getOffset())
			.orderBy(getOrderSpecifier(planetPage))
			.fetch();
	}

	private OrderSpecifier<?> getOrderSpecifier(PlanetPage planetPage) {
		Order direction = planetPage.isAscending() ? Order.ASC : Order.DESC;
		String prop = planetPage.getProperty();
		PathBuilder orderByExpression = new PathBuilder(Planet.class, "planet");

		return new OrderSpecifier(direction, orderByExpression.get(prop));
	}
}

package com.planetlandV2.Reader;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.request.PlanetPage;
import com.planetlandV2.response.PlanetResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlanetReaderImpl implements PlanetReader {

	private final PlanetRepository planetRepository;

	@Override
	public Planet read(Long planetId) {
		return planetRepository.findById(planetId)
			.orElseThrow(PlanetNotFound::new);
	}

	@Override
	public List<PlanetResponse> readList(PlanetPage planetPage) {
		return planetRepository.getList(planetPage).stream()
			.map(Planet::toResponse)
			.collect(Collectors.toList());
	}

	@Override
	public Planet readForBuy(Long planetId) {
		return planetRepository.findByIdForBuy(planetId)
			.orElseThrow(PlanetNotFound::new);
	}
}

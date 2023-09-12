package com.planetlandV2.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.image.CreateFile;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.requset.PlanetCreate;
import com.planetlandV2.requset.PlanetEdit;
import com.planetlandV2.requset.PlanetPage;
import com.planetlandV2.response.PlanetResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetService {

	private final PlanetRepository planetRepository;

	public void create(PlanetCreate request, MultipartFile imgFile) throws IOException {

		String imgName = CreateFile.getImgName(imgFile);

		Planet planet = Planet.builder()
			.planetName(request.getPlanetName())
			.price(request.getPrice())
			.population(request.getPopulation())
			.satellite(request.getSatellite())
			.planetStatus(request.getPlanetStatus())
			.imgName(imgName)
			.imgPath(CreateFile.PATH + imgName)
			.build();

		planetRepository.save(planet);
	}

	public PlanetResponse get(Long planetId) {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));

		return PlanetResponse.builder()
			.id(planet.getPlanetId())
			.planetName(planet.getPlanetName())
			.price(planet.getPrice())
			.population(planet.getPopulation())
			.satellite(planet.getSatellite())
			.planetStatus(planet.getPlanetStatus())
			.imgName(planet.getImgName())
			.imgPath(planet.getImgPath())
			.build();
	}

	@Transactional
	public void edit(Long planetId, PlanetEdit planetEdit, MultipartFile imgFile) throws IOException {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));

		if (imgFile != null) {
			String imgName = CreateFile.getImgName(imgFile);
			String imgPath = CreateFile.PATH + imgName;

			planet.imgEdit(imgName, imgPath);
		}

		planet.edit(planetEdit);
	}

	public void delete(Long planetId) {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));

		planetRepository.deleteById(planet.getPlanetId());
	}

	public List<PlanetResponse> getList(PlanetPage planetPage) {
		return planetRepository.getList(planetPage).stream()
			.map(planet -> new PlanetResponse(planet))
			.collect(Collectors.toList());
	}
}

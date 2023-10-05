package com.planetlandV2.service;

import static com.planetlandV2.Constants.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.exception.InvalidRequest;
import com.planetlandV2.exception.PlanetNotFound;
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

		String imgName = getImgName(imgFile);

		Planet planet = Planet.builder()
			.planetName(request.getPlanetName())
			.price(request.getPrice())
			.population(request.getPopulation())
			.satellite(request.getSatellite())
			.planetStatus(request.getPlanetStatus())
			.imgName(imgName)
			.imgPath(PATH + imgName)
			.build();

		planetRepository.save(planet);
	}

	public PlanetResponse get(Long planetId) {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

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
			.orElseThrow(() -> new PlanetNotFound());

		if (imgFile != null) {
			String imgName = getImgName(imgFile);
			String imgPath = PATH + imgName;

			planet.imgEdit(imgName, imgPath);
		}

		planet.edit(planetEdit);
	}

	public void delete(Long planetId) {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

		planetRepository.deleteById(planet.getPlanetId());
	}

	public List<PlanetResponse> getList(PlanetPage planetPage) {
		return planetRepository.getList(planetPage).stream()
			.map(planet -> new PlanetResponse(planet))
			.collect(Collectors.toList());
	}

	public String checkDuplicate(String planetName) {
		boolean exists = planetRepository.existsByPlanetName(planetName);
		if (exists){
			throw new InvalidRequest("planetName", "이미 존재하는 행성입니다.");
		}
		return "사용 가능한 행성 이름입니다.";
	}

	private String getImgName(MultipartFile imgFile) throws IOException {
		String originName = imgFile.getOriginalFilename();

		UUID uuid = UUID.randomUUID();
		String imgName = uuid + "_" + originName;

		imgFile.transferTo(new File(PATH + imgName));
		return imgName;
	}
}

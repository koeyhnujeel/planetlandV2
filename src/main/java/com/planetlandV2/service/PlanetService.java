package com.planetlandV2.service;

import static com.planetlandV2.Constants.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.exception.ExistsPlanetNameException;
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

	public void create(PlanetCreate planetCreate, MultipartFile imgFile) throws IOException {
		checkPlanetName(planetCreate.getPlanetName());
		String imgName = getImgName(imgFile);
		Planet planet = planetCreate.toEntity(imgName);
		planetRepository.save(planet);
	}

	public PlanetResponse get(Long planetId) {

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());
		return planet.toResponse();
	}

	public void edit(Long planetId, PlanetEdit planetEdit, MultipartFile imgFile) throws IOException {
		checkPlanetName(planetEdit.getPlanetName());
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

		if (imgFile != null) {
			String imgName = getImgName(imgFile);
			String imgPath = PATH + imgName;

			planet.imgEdit(imgName, imgPath);
		}
		planet.edit(planetEdit);
		planetRepository.save(planet);
	}

	public void delete(Long planetId) {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

		planetRepository.deleteById(planet.getPlanetId());
	}

	public List<PlanetResponse> getList(PlanetPage planetPage) {
		return planetRepository.getList(planetPage).stream()
			.map(planet -> planet.toResponse())
			.collect(Collectors.toList());
	}

	public void checkPlanetName(String planetName) {
		boolean exists = planetRepository.existsByPlanetName(planetName);
		if (exists){
			throw new ExistsPlanetNameException();
		}
	}

	private String getImgName(MultipartFile imgFile) throws IOException {
		String originName = imgFile.getOriginalFilename();

		UUID uuid = UUID.randomUUID();
		String imgName = uuid + "_" + originName;

		imgFile.transferTo(new File(PATH + imgName));
		return imgName;
	}
}

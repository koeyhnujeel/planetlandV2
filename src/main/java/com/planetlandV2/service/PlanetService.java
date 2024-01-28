package com.planetlandV2.service;

import static com.planetlandV2.image.ImageProcess.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.exception.planet.ExistsPlanetNameException;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.image.ImageProcess;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.request.PlanetCreate;
import com.planetlandV2.request.PlanetEdit;
import com.planetlandV2.request.PlanetPage;
import com.planetlandV2.response.PlanetResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetService {

	private final PlanetRepository planetRepository;
	private final ImageProcess imageProcess;

	public void create(PlanetCreate planetCreate, MultipartFile imgFile) throws IOException {
		checkPlanetName(planetCreate.getPlanetName());
		String imgName = imageProcess.getImageNameAndSave(imgFile);
		Planet planet = planetCreate.toEntity(imgName);
		planetRepository.save(planet);
	}

	public PlanetResponse get(Long planetId) {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());
		return planet.toResponse();
	}

	@Transactional
	public void edit(Long planetId, PlanetEdit planetEdit, MultipartFile imgFile) throws IOException {
		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

		if (!planet.getPlanetName().equals(planetEdit.getPlanetName())) {
			checkPlanetName(planetEdit.getPlanetName());
		}

		if (!imgFile.isEmpty()) {
			imageProcess.CheckExtension(imgFile);
			String imgName = imageProcess.getImageNameAndSave(imgFile);
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
			.map(planet -> planet.toResponse())
			.collect(Collectors.toList());
	}

	private void checkPlanetName(String planetName) {
		boolean exists = planetRepository.existsByPlanetName(planetName);
		if (exists){
			throw new ExistsPlanetNameException();
		}
	}
}

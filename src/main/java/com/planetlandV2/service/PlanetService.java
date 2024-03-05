package com.planetlandV2.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.Manager.PlanetManager;
import com.planetlandV2.Manager.TransactionManager;
import com.planetlandV2.Reader.PlanetReader;
import com.planetlandV2.exception.planet.ExistsPlanetNameException;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.request.PlanetCreate;
import com.planetlandV2.request.PlanetEdit;
import com.planetlandV2.request.PlanetPage;
import com.planetlandV2.response.PlanetDetailResponse;
import com.planetlandV2.response.PlanetResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetService {

	private final PlanetReader planetReader;
	private final PlanetManager planetManager;
	private final TransactionManager transactionManager;
	private final PlanetRepository planetRepository;

	public void addPlanet(PlanetCreate planetCreate, MultipartFile imgFile) throws IOException {
		checkPlanetName(planetCreate.getPlanetName());
		planetManager.create(planetCreate, imgFile);
	}

	public PlanetDetailResponse findPlanet(Long planetId) {
		Planet planet = planetReader.read(planetId);
		return planet.toDetailResponse();
	}

	@Transactional
	public void modifyPlanet(Long planetId, PlanetEdit planetEdit, MultipartFile imgFile) throws IOException {
		Planet planet = planetReader.read(planetId);

		if (!planet.getPlanetName().equals(planetEdit.getPlanetName())) {
			checkPlanetName(planetEdit.getPlanetName());
		}

		planetManager.update(planet,planetEdit,imgFile);
	}

	@Transactional
	public void removePlanet(Long planetId) {
		Planet planet = planetReader.read(planetId);
		transactionManager.removePlanet(planet);
		planetManager.remove(planet.getPlanetId());
	}

	public List<PlanetResponse> findPlanetList(PlanetPage planetPage) {
		return planetReader.readList(planetPage);
	}

	private void checkPlanetName(String planetName) {
		boolean exists = planetRepository.existsByPlanetName(planetName);
		if (exists){
			throw new ExistsPlanetNameException();
		}
	}
}

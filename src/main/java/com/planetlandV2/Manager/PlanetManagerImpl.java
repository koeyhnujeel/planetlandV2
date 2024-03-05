package com.planetlandV2.Manager;

import static com.planetlandV2.image.ImageProcess.*;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.image.ImageProcess;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.request.PlanetCreate;
import com.planetlandV2.request.PlanetEdit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlanetManagerImpl implements PlanetManager {

	private final PlanetRepository planetRepository;
	private final ImageProcess imageProcess;

	@Override
	public void create(PlanetCreate planetCreate, MultipartFile imgFile) throws IOException {
		String imgName = imageProcess.getImageNameAndSave(imgFile);
		Planet planet = planetCreate.toEntity(imgName);
		planetRepository.save(planet);
	}

	@Override
	public void update(Planet planet, PlanetEdit planetEdit, MultipartFile imgFile) throws IOException {
		if (imgFile != null) {
			imageProcess.CheckExtension(imgFile);
			String imgName = imageProcess.getImageNameAndSave(imgFile);
			String imgPath = PATH + imgName;
			planet.imgEdit(imgName, imgPath);
		}
		planet.edit(planetEdit);
	}

	@Override
	public void remove(Long planetId) {
		planetRepository.deleteById(planetId);
	}
}

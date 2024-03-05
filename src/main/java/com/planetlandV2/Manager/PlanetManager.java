package com.planetlandV2.Manager;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.request.PlanetCreate;
import com.planetlandV2.request.PlanetEdit;

public interface PlanetManager {

	void create(PlanetCreate planetCreate, MultipartFile imgFile) throws IOException;

	void update(Planet planet, PlanetEdit planetEdit, MultipartFile imgFile) throws IOException;

	void remove(Long planetId);
}

package com.planetlandV2.controller;

import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.image.ImageProcess;
import com.planetlandV2.request.PlanetCreate;
import com.planetlandV2.request.PlanetEdit;
import com.planetlandV2.request.PlanetPage;
import com.planetlandV2.response.PlanetDetailResponse;
import com.planetlandV2.response.PlanetResponse;
import com.planetlandV2.service.PlanetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanetController {

	private final PlanetService planetService;
	private final ImageProcess imageProcess;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/planets")
	public void planetAdd(@RequestPart @Valid PlanetCreate planetCreate, @RequestPart MultipartFile imgFile) throws IOException {
		imageProcess.CheckExtension(imgFile);
		planetCreate.validate();
		planetService.addPlanet(planetCreate, imgFile);
	}

	@GetMapping("/planets/{planetId}")
	public PlanetDetailResponse planetDetails(@PathVariable Long planetId) {
		return planetService.findPlanet(planetId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping("/planets/{planetId}")
	public void planetModify(@PathVariable Long planetId, @RequestPart @Valid PlanetEdit planetEdit, @RequestPart(required = false) MultipartFile imgFile) throws IOException {
		planetEdit.validate();
		planetService.modifyPlanet(planetId, planetEdit, imgFile);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/planets/{planetId}")
	public void planetRemove(@PathVariable Long planetId) {
		planetService.removePlanet(planetId);
	}

	@GetMapping("/planets")
	public List<PlanetResponse> planetList(@ModelAttribute PlanetPage planetPage) {
		return planetService.findPlanetList(planetPage);
	}
}

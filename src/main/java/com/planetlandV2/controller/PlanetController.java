package com.planetlandV2.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.config.data.UserSession;
import com.planetlandV2.exception.ImageFileNotFound;
import com.planetlandV2.image.ImageProcess;
import com.planetlandV2.requset.PlanetCreate;
import com.planetlandV2.requset.PlanetEdit;
import com.planetlandV2.requset.PlanetPage;
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

	@GetMapping("/foo")
	public Long foo(UserSession userSession) {
		log.info(">>>{}", userSession.id);
		return userSession.id;
	}

	@PostMapping("/planets")
	public void create(@RequestPart @Valid PlanetCreate planetCreate, @RequestPart MultipartFile imgFile) throws IOException {
		if (imgFile.isEmpty()) throw new ImageFileNotFound();
		imageProcess.CheckExtension(imgFile);
		planetCreate.validate();
		planetService.create(planetCreate, imgFile);
	}

	@GetMapping("/planets/{planetId}")
	public PlanetResponse get(@PathVariable Long planetId) {
		return planetService.get(planetId);
	}

	@PatchMapping("/planets/{planetId}")
	public void edit(@PathVariable Long planetId, @RequestPart @Valid PlanetEdit planetEdit, @RequestPart(required = false) MultipartFile imgFile) throws IOException {
		planetEdit.validate();
		planetService.edit(planetId, planetEdit, imgFile);
	}

	@DeleteMapping("/planets/{planetId}")
	public void delete(@PathVariable Long planetId) {
		planetService.delete(planetId);
	}

	@GetMapping("/planets")
	public List<PlanetResponse> getList(@ModelAttribute PlanetPage planetPage) {
		return planetService.getList(planetPage);
	}
}

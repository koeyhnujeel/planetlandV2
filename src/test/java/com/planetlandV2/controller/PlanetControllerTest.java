package com.planetlandV2.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.requset.PlanetCreate;
import com.planetlandV2.requset.PlanetEdit;
import com.planetlandV2.service.PlanetService;

@SpringBootTest
@AutoConfigureMockMvc
class PlanetControllerTest {

	@Autowired
	private PlanetService planetService;

	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("행성 생성 요청 시 DB에 저장된다.")
	void test1() throws Exception {
		//given
		PlanetCreate planetCreate = PlanetCreate.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();

		String json = objectMapper.writeValueAsString(planetCreate);

		MockMultipartFile imgFile = new MockMultipartFile("imgFile", "test.png", "image/png", "png".getBytes());

		MockMultipartFile request = new MockMultipartFile("request", null,
			"application/json", json.getBytes(StandardCharsets.UTF_8));

		//expected
		mockMvc.perform(multipart(HttpMethod.POST, "/planets")
					.file(request)
					.file(imgFile)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("행성 1개 조회")
	void test2() throws Exception {
		//given
		Planet planet = Planet.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.imgName("testImg")
			.imgPath("/testPath")
			.build();
		planetRepository.save(planet);

		// expected
		mockMvc.perform(get("/planets/{planetId}", planet.getPlanetId())
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("행성 수정")
	void test3() throws Exception {
		//given
		Planet planet = Planet.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		PlanetEdit edit = PlanetEdit.builder()
			.planetName("수정된 행성")
			.price(32432)
			.population(5321312)
			.satellite(32312)
			.planetStatus("구매 불가")
			.build();

		String json = objectMapper.writeValueAsString(edit);

		MockMultipartFile imgFile = new MockMultipartFile("imgFile", "test.png", "image/png", "png".getBytes());

		MockMultipartFile planetEdit = new MockMultipartFile("planetEdit", null,
			"application/json", json.getBytes(StandardCharsets.UTF_8));

		//expected
		mockMvc.perform(multipart(HttpMethod.PATCH, "/planets/{planetId}", planet.getPlanetId())
				.file(planetEdit)
				.file(imgFile)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("행성 삭제")
	void test4() throws Exception {
		//given
		Planet planet = Planet.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.imgName("testImg")
			.imgPath("/testPath")
			.build();
		planetRepository.save(planet);

		// expected
		mockMvc.perform(delete("/planets/{planetId}", planet.getPlanetId())
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}
}

package com.planetlandV2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import javax.validation.constraints.Null;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.support.NullValue;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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

		//when
		mockMvc.perform(multipart(HttpMethod.POST, "/planets")
					.file(request)
					.file(imgFile)
			)
			.andExpect(status().isOk())
			.andDo(print());

		//then
		assertEquals(1L, planetRepository.count());

		Planet planet = planetRepository.findAll().get(0);
		assertEquals("지구", planet.getPlanetName());
		assertEquals(10000, planet.getPrice());
		assertEquals(5000, planet.getPopulation());
		assertEquals(1, planet.getSatellite());
		assertEquals("구매 가능", planet.getPlanetStatus());
		assertNotNull(planet.getImgName());
		assertNotNull(planet.getImgPath());
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
			.andExpect(jsonPath("$.id").value(planet.getPlanetId()))
			.andExpect(jsonPath("$.planetName").value(planet.getPlanetName()))
			.andExpect(jsonPath("$.price").value(planet.getPrice()))
			.andExpect(jsonPath("$.population").value(planet.getPopulation()))
			.andExpect(jsonPath("$.satellite").value(planet.getSatellite()))
			.andExpect(jsonPath("$.planetStatus").value(planet.getPlanetStatus()))
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

		//when
		mockMvc.perform(multipart(HttpMethod.PATCH, "/planets/{planetId}", planet.getPlanetId())
				.file(planetEdit)
				.file(imgFile)
			)
			.andExpect(status().isOk())
			.andDo(print());

		//then
		Planet changedPlanet = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));

		assertEquals("수정된 행성", changedPlanet.getPlanetName());
		assertEquals(32432, changedPlanet.getPrice());
		assertEquals(5321312, changedPlanet.getPopulation());
		assertEquals(32312, changedPlanet.getSatellite());
		assertEquals("구매 불가", changedPlanet.getPlanetStatus());
		assertNotNull(changedPlanet.getImgName());
		assertNotNull(changedPlanet.getImgPath());
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

	@Test
	@DisplayName("행성 생성 시 행성 이름, 가격, 인구수, 위성수, 구매가능 여부값은 필수다.")
	void test5() throws Exception {
		//given
		PlanetCreate planetCreate = PlanetCreate.builder()
			.planetName("")
			.price(null)
			.population(null)
			.satellite(null)
			.planetStatus("")
			.build();

		String json = objectMapper.writeValueAsString(planetCreate);

		MockMultipartFile imgFile = new MockMultipartFile("imgFile", "test.png", "image/png", "png".getBytes());

		MockMultipartFile request = new MockMultipartFile("request", null,
			"application/json", json.getBytes(StandardCharsets.UTF_8));

		//expected
		mockMvc.perform(multipart(HttpMethod.POST, "/planets")
				.file(imgFile)
				.file(request)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validation.planetName").value("행성 이름을 입력해주세요."))
			.andExpect(jsonPath("$.validation.price").value("행성 가격을 입력해주세요."))
			.andExpect(jsonPath("$.validation.population").value("행성 인구수를 입력해주세요."))
			.andExpect(jsonPath("$.validation.satellite").value("위성수를 입력해주세요."))
			.andExpect(jsonPath("$.validation.planetStatus").value("구매가능 여부를 입력해주세요."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 생성 시 행성 최소 가격은 1원이다.")
	void test6() throws Exception {
		//given
		PlanetCreate planetCreate = PlanetCreate.builder()
			.planetName("test")
			.price(0)
			.population(0)
			.satellite(0)
			.planetStatus("구매 가능")
			.build();

		String json = objectMapper.writeValueAsString(planetCreate);

		MockMultipartFile imgFile = new MockMultipartFile("imgFile", "test.png", "image/png", "png".getBytes());

		MockMultipartFile request = new MockMultipartFile("request", null,
			"application/json", json.getBytes(StandardCharsets.UTF_8));

		//expected
		mockMvc.perform(multipart(HttpMethod.POST, "/planets")
				.file(imgFile)
				.file(request)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validation.price").value("행성 최소 가격은 1원입니다."))
			.andDo(print());
	}
}

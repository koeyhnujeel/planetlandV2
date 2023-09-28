package com.planetlandV2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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

	@BeforeEach
	void clean() {
		planetRepository.deleteAll();
	}

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
	@DisplayName("행성 리스트 조회 - 최신순")
	void test3() throws Exception {
		// expected
		List<Planet> planets = IntStream.range(0, 20)
			.mapToObj(i -> Planet.builder()
				.planetName("행성" + i)
				.price(1 + i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets);

		mockMvc.perform(get("/planets?page=1&size=10&keyword=최신순")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", Matchers.is(10)))
			.andExpect(jsonPath("$[0].id").value(planets.get(19).getPlanetId()))
			.andExpect(jsonPath("$[0].planetName").value("행성19"))
			.andExpect(jsonPath("$[0].price").value(20))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 리스트 조회 - 과거순")
	void test4() throws Exception {
		// expected
		List<Planet> planets = IntStream.range(0, 20)
			.mapToObj(i -> Planet.builder()
				.planetName("행성" + i)
				.price(1 + i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets);

		mockMvc.perform(get("/planets?page=1&size=10&keyword=과거순")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", Matchers.is(10)))
			.andExpect(jsonPath("$[0].id").value(planets.get(0).getPlanetId()))
			.andExpect(jsonPath("$[0].planetName").value("행성0"))
			.andExpect(jsonPath("$[0].price").value(1))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 리스트 조회 - 높은 가치순")
	void test5() throws Exception {
		// expected
		List<Planet> planets1 = IntStream.range(0, 10)
			.mapToObj(i -> Planet.builder()
				.planetName("행성 " + i)
				.price(1000 - i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets1);

		Planet target = planetRepository.save(Planet.builder()
			.planetName("제일 비싼 행성")
			.price(100000)
			.build());

		List<Planet> planets2 = IntStream.range(11, 20)
			.mapToObj(i -> Planet.builder()
				.planetName("행성 " + i)
				.price(1000 + i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets2);

		mockMvc.perform(get("/planets?page=1&size=10&keyword=높은 가치순")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", Matchers.is(10)))
			.andExpect(jsonPath("$[0].id").value(target.getPlanetId()))
			.andExpect(jsonPath("$[0].planetName").value("제일 비싼 행성"))
			.andExpect(jsonPath("$[0].price").value(100000))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 리스트 조회 - 낮은 가치순")
	void test6() throws Exception {
		// expected
		List<Planet> planets1 = IntStream.range(0, 10)
			.mapToObj(i -> Planet.builder()
				.planetName("행성 " + i)
				.price(1000 - i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets1);

		Planet target = planetRepository.save(Planet.builder()
			.planetName("제일 싼 행성")
			.price(1)
			.build());

		List<Planet> planets2 = IntStream.range(11, 20)
			.mapToObj(i -> Planet.builder()
				.planetName("행성 " + i)
				.price(1000 + i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets2);

		mockMvc.perform(get("/planets?page=1&size=10&keyword=낮은 가치순")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", Matchers.is(10)))
			.andExpect(jsonPath("$[0].id").value(target.getPlanetId()))
			.andExpect(jsonPath("$[0].planetName").value("제일 싼 행성"))
			.andExpect(jsonPath("$[0].price").value(1))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 수정")
	void test7() throws Exception {
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
	void test8() throws Exception {
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
	void test9() throws Exception {
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
	void test10() throws Exception {
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

	@Test
	@DisplayName("행성 생성 시 이미지 파일 업로드는 필수다.")
	void test11() throws Exception {
		//given
		PlanetCreate planetCreate = PlanetCreate.builder()
			.planetName("test")
			.price(1)
			.population(0)
			.satellite(0)
			.planetStatus("구매 가능")
			.build();

		String json = objectMapper.writeValueAsString(planetCreate);

		MockMultipartFile request = new MockMultipartFile("request", null,
			"application/json", json.getBytes(StandardCharsets.UTF_8));

		//expected
		mockMvc.perform(multipart(HttpMethod.POST, "/planets")
				.file(request)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validation.imgFile").value("이미지 파일을 업로드 해주세요."))
			.andDo(print());
	}

	@Test
	@DisplayName("존재하지 않는 행성 조회")
	void test12() throws Exception {
		// expected
		mockMvc.perform(get("/planets/{planetId}", 909)
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isNotFound())
			.andDo(print());
	}

	@Test
	@DisplayName("존재하지 않는 행성 수정")
	void test13() throws Exception {
		//given
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
		mockMvc.perform(multipart(HttpMethod.PATCH, "/planets/{planetId}", 10L)
				.file(planetEdit)
				.file(imgFile)
			)
			.andExpect(status().isNotFound())
			.andDo(print());
	}

	@Test
	@DisplayName("존재하지 않는 행성 삭제")
	void test14() throws Exception {
		// expected
		mockMvc.perform(delete("/planets/{planetId}", 10L)
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isNotFound())
			.andDo(print());
	}

	@Test
	@DisplayName("행성 생성 시 행성 이름에 비속어는 포함될 수 없다.")
	void test15() throws Exception {
		//given
		PlanetCreate planetCreate = PlanetCreate.builder()
			.planetName("바보")
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
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validation.planetName").value("행성 이름에 비속어는 포함할 수 없습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 수정 시 행성 이름에 비속어는 포함될 수 없다.")
	void test16() throws Exception {
		//given
		Planet planet = Planet.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		PlanetEdit planetEdit = PlanetEdit.builder()
			.planetName("바보")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();

		String json = objectMapper.writeValueAsString(planetEdit);

		MockMultipartFile imgFile = new MockMultipartFile("imgFile", "test.png", "image/png", "png".getBytes());

		MockMultipartFile request = new MockMultipartFile("planetEdit", null,
			"application/json", json.getBytes(StandardCharsets.UTF_8));

		//expected
		mockMvc.perform(multipart(HttpMethod.PATCH, "/planets/{planetId}", planet.getPlanetId())
				.file(request)
				.file(imgFile)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validation.planetName").value("행성 이름에 비속어는 포함할 수 없습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성이름은 중복 될 수 없다 - 중복 일 때)")
	void test17() throws Exception {
		//given
		Planet planet = Planet.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		//expected
		mockMvc.perform(get("/planets/exists/{planetName}", "지구")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validation.planetName").value("이미 존재하는 행성입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성이름은 중복 될 수 없다 - 중복 아닐 때)")
	void test18() throws Exception {
		//given
		Planet planet = Planet.builder()
			.planetName("지구")
			.price(10000)
			.population(5000)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		//expected
		mockMvc.perform(get("/planets/exists/{planetName}", "화성")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("사용 가능한 행성 이름입니다."))
			.andDo(print());
	}
}

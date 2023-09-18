package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.exception.PlanetNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.requset.PlanetCreate;
import com.planetlandV2.requset.PlanetEdit;
import com.planetlandV2.requset.PlanetPage;
import com.planetlandV2.response.PlanetResponse;

@SpringBootTest
class PlanetServiceTest {

	@BeforeEach
	void clean() {
		planetRepository.deleteAll();
	}

	@Autowired
	private PlanetService planetService;

	@Autowired
	private PlanetRepository planetRepository;

	@Test
	@DisplayName("행성 생성하기")
	void test1() throws IOException {
		//given
		PlanetCreate request = PlanetCreate.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();

		MultipartFile imgFile = new MockMultipartFile("files", "imgFile.jpeg", "image/jpeg",
			"<<jpeg data>>".getBytes());

		// when
		planetService.create(request, imgFile);

		//then
		assertEquals(1, planetRepository.count());
		Planet planet = planetRepository.findAll().get(0);
		assertEquals("테스트 행성1", planet.getPlanetName());
		assertEquals(1000, planet.getPrice());
		assertEquals(100, planet.getPopulation());
		assertEquals(1, planet.getSatellite());
		assertEquals("구매 가능", planet.getPlanetStatus());
		System.out.println(planet.getImgName());
		System.out.println(planet.getImgPath());
		assertNotNull(planet.getImgName());
		assertNotNull(planet.getImgPath());
	}

	@Test
	@DisplayName("행성 1개 조회")
	void test2() throws Exception{
		// given
		Planet planet = Planet.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		// when
		PlanetResponse planetResponse = planetService.get(planet.getPlanetId());

		// then
		assertNotNull(planetResponse);
		assertEquals("테스트 행성1", planetResponse.getPlanetName());
		assertEquals(1000, planetResponse.getPrice());
		assertEquals(100, planetResponse.getPopulation());
		assertEquals(1, planetResponse.getSatellite());
		assertEquals("구매 가능", planetResponse.getPlanetStatus());
	}

	@Test
	@DisplayName("행성 수정하기")
	void test3() throws IOException {
		//given
		Planet planet = Planet.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		PlanetEdit planetEdit = PlanetEdit.builder()
			.planetName("수정된 행성")
			.price(2000)
			.population(200)
			.satellite(3)
			.planetStatus("구매 가능")
			.build();

		MultipartFile imgFile = new MockMultipartFile("files", "imgFile.jpeg", "image/jpeg",
			"<<jpeg data>>".getBytes());

		// when
		planetService.edit(planet.getPlanetId(), planetEdit, imgFile);

		//then
		Planet changedPlanet = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));

		assertEquals("수정된 행성", changedPlanet.getPlanetName());
		assertEquals(2000, changedPlanet.getPrice());
		assertEquals(200, changedPlanet.getPopulation());
		assertEquals(3, changedPlanet.getSatellite());
		assertEquals("구매 가능", changedPlanet.getPlanetStatus());
		System.out.println(changedPlanet.getImgName());
		System.out.println(changedPlanet.getImgPath());
		assertNotNull(changedPlanet.getImgName());
		assertNotNull(changedPlanet.getImgPath());
	}

	@Test
	@DisplayName("행성 삭제하기")
	void test4() {
		//given
		Planet planet = Planet.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		//when
		planetService.delete(planet.getPlanetId());

		//then
		assertEquals(0, planetRepository.count());
	}
	@Test
	@DisplayName("행성 목록 조회 - 최신순")
	void test5() {
		// given
		List<Planet> planets = IntStream.range(0, 20)
			.mapToObj(i -> Planet.builder()
				.planetName("행성 " + i)
				.price(1000 + i)
				.build())
			.collect(Collectors.toList());
		planetRepository.saveAll(planets);

		// when
		PlanetPage planetPage = PlanetPage.builder()
			.page(2)
			.size(10)
			.build();

		// then
		List<PlanetResponse> list = planetService.getList(planetPage);

		assertEquals(10L, list.size());
		assertEquals("행성 0", list.get(9).getPlanetName());
	}

	@Test
	@DisplayName("행성 1개 조회 - 존재하지 않는 행성")
	void test6() throws Exception{
		// given
		Planet planet = Planet.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.imgName("test.png")
			.imgPath("/testPath")
			.build();
		planetRepository.save(planet);

		//expected
		assertThrows(PlanetNotFound.class, () -> {
			planetService.get(planet.getPlanetId() + 1);
		});
	}

	@Test
	@DisplayName("행성 수정하기 - 존재하지 않는 행성")
	void test7() throws IOException {
		//given
		Planet planet = Planet.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.imgName("origin.png")
			.imgPath("/originPath")
			.build();
		planetRepository.save(planet);

		PlanetEdit planetEdit = PlanetEdit.builder()
			.planetName("수정된 행성")
			.price(2000)
			.population(200)
			.satellite(3)
			.planetStatus("구매 가능")
			.build();

		MultipartFile imgFile = new MockMultipartFile("files", "imgFile.jpeg", "image/jpeg",
			"<<jpeg data>>".getBytes());

		//expected
		assertThrows(PlanetNotFound.class, () -> {
			planetService.edit(planet.getPlanetId() + 1, planetEdit, imgFile);
		});
	}

	@Test
	@DisplayName("행성 삭제하기 - 존재하지 않는 행성")
	void test8() {
		//given
		Planet planet = Planet.builder()
			.planetName("테스트 행성1")
			.price(1000)
			.population(100)
			.satellite(1)
			.planetStatus("구매 가능")
			.build();
		planetRepository.save(planet);

		//expected
		assertThrows(PlanetNotFound.class, () -> {
			planetService.delete(planet.getPlanetId() + 1);
		});
	}
}

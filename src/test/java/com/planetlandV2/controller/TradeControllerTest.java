package com.planetlandV2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.SessionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.PlanetSell;

@SpringBootTest
@AutoConfigureMockMvc
class TradeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@AfterEach
	void clean() {
		userRepository.deleteAll();
		planetRepository.deleteAll();
		sessionRepository.deleteAll();
	}

	@Test
	@DisplayName("판매등록 성공")
	void test1() throws Exception {
		// given
		User user = User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.balance(10000)
			.build();
		userRepository.save(user);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(1)
			.owner(user.getNickname())
			.planetStatus(PlanetStatus.NOTFORSALE)
			.build();
		planetRepository.save(planet);

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(5000)
			.build();

		user.getPlanets().add(planet);
		Session session = user.addSession();
		sessionRepository.save(session);

		String json = objectMapper.writeValueAsString(planetSell);
		Cookie cookie = new MockCookie("SESSION", session.getAccessToken());

		//expected
		mockMvc.perform(patch("/planets/{planetId}/sell", planet.getPlanetId())
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andDo(print());

		Planet targetPlanet = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(() -> new PlanetNotFound());

		assertEquals(5000, targetPlanet.getPrice());
		assertEquals(PlanetStatus.FORSALE, targetPlanet.getPlanetStatus());
	}

	@Test
	@DisplayName("판매등록 실패 - 로그인 안했을 때")
	void test2() throws Exception {
		// given
		User user = User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.balance(10000)
			.build();
		userRepository.save(user);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(1)
			.owner(user.getNickname())
			.planetStatus(PlanetStatus.NOTFORSALE)
			.build();
		planetRepository.save(planet);

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(5000)
			.build();

		user.getPlanets().add(planet);

		String json = objectMapper.writeValueAsString(planetSell);

		//expected
		mockMvc.perform(patch("/planets/{planetId}/sell", planet.getPlanetId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("로그인이 필요한 서비스입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성구매 성공")
	void test3() throws Exception {
		// given
		User buyer = User.builder()
			.email("test1@email.com")
			.password("1234")
			.nickname("buyer")
			.balance(10000)
			.build();
		userRepository.save(buyer);

		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
			.balance(10000)
			.build();
		userRepository.save(seller);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(5000)
			.owner(seller.getNickname())
			.planetStatus(PlanetStatus.FORSALE)
			.build();
		planetRepository.save(planet);

		seller.getPlanets().add(planet);
		Session session = buyer.addSession();
		sessionRepository.save(session);

		Cookie cookie = new MockCookie("SESSION", session.getAccessToken());

		//expected
		mockMvc.perform(patch("/planets/{planetId}/buy", planet.getPlanetId())
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON)
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.planetName").value("test"))
			.andExpect(jsonPath("$.price").value(5000))
			.andExpect(jsonPath("$.message").value("구매가 완료되었습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성구매 실패 - 로그인 안했을 때")
	void test4() throws Exception {
		// given
		User buyer = User.builder()
			.email("test1@email.com")
			.password("1234")
			.nickname("buyer")
			.balance(10000)
			.build();
		userRepository.save(buyer);

		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
			.balance(10000)
			.build();
		userRepository.save(seller);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(5000)
			.owner(seller.getNickname())
			.planetStatus(PlanetStatus.FORSALE)
			.build();
		planetRepository.save(planet);

		seller.getPlanets().add(planet);

		//expected
		mockMvc.perform(patch("/planets/{planetId}/buy", planet.getPlanetId())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("로그인이 필요한 서비스입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("행성 판매 취소 성공")
	void test5() throws Exception {
		// given
		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
			.balance(10000)
			.build();
		userRepository.save(seller);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(5000)
			.owner(seller.getNickname())
			.planetStatus(PlanetStatus.FORSALE)
			.build();
		planetRepository.save(planet);

		seller.getPlanets().add(planet);
		Session session = seller.addSession();
		sessionRepository.save(session);

		Cookie cookie = new MockCookie("SESSION", session.getAccessToken());

		//expected
		mockMvc.perform(patch("/planets/{planetId}/sellCancel", planet.getPlanetId())
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}
}

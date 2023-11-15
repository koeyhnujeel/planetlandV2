package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.config.data.UserSession;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.NotOwnerException;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.TradeHistoryRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.PlanetSell;

@SpringBootTest
class TradeServiceTest {

	@Autowired
	private TradeHistoryRepository tradeHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private TradeService tradeService;


	@AfterEach
	void clean() {
		userRepository.deleteAll();
		planetRepository.deleteAll();
		tradeHistoryRepository.deleteAll();
	}

	@Test
	@DisplayName("판매등록 성공")
	@Transactional
	void test1() {
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
			.price(5000)
			.owner(user.getNickname())
			.planetStatus(PlanetStatus.NOTFORSALE)
			.build();
		planetRepository.save(planet);

		user.getPlanets().add(planet);
		Session session = user.addSession();
		UserSession userSession = new UserSession(session.getUser().getId());

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(6000)
			.build();

		// when
		tradeService.sell(userSession, planet.getPlanetId(), planetSell);

		// then
		assertEquals(6000, planet.getPrice());
		assertEquals(PlanetStatus.FORSALE, planet.getPlanetStatus());
	}

	@Test
	@DisplayName("판매등록 실패 - 본인 소유 행성이 아닐 때")
	void test2() {
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
			.price(5000)
			.owner("other")
			.planetStatus(PlanetStatus.NOTFORSALE)
			.build();
		planetRepository.save(planet);

		user.getPlanets().add(planet);
		Session session = user.addSession();
		UserSession userSession = new UserSession(session.getUser().getId());

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(6000)
			.build();

		// expected
		assertThrows(NotOwnerException.class,
			() -> tradeService.sell(userSession, planet.getPlanetId(), planetSell));
	}
}

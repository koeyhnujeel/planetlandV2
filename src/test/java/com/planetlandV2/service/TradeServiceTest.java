package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.Enum.TradeType;
import com.planetlandV2.config.data.UserSession;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.TradeHistory;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.NotEnoughBalance;
import com.planetlandV2.exception.NotOwnerException;
import com.planetlandV2.exception.PlanetNotFound;
import com.planetlandV2.exception.UserNotFound;
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
		Planet targetPlanet = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(() -> new PlanetNotFound());

		assertEquals(6000, targetPlanet.getPrice());
		assertEquals(PlanetStatus.FORSALE, targetPlanet.getPlanetStatus());
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

	@Test
	@DisplayName("행성구매 성공")
	@Transactional
	void test3() {
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
		UserSession userSession = new UserSession(session.getUser().getId());

		// when
		tradeService.buy(userSession, planet.getPlanetId());

		// then
		User targetBuyer = userRepository.findById(buyer.getId())
			.orElseThrow(() -> new UserNotFound());

		User targetSeller = userRepository.findById(seller.getId())
			.orElseThrow(() -> new UserNotFound());

		Planet targetPlanet = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(() -> new PlanetNotFound());

		assertEquals(1, targetBuyer.getPlanets().size());
		assertEquals("test", targetBuyer.getPlanets().get(0).getPlanetName());
		assertEquals(5000, targetBuyer.getBalance());
		assertEquals(1, targetBuyer.getTradeHistoryList().size());
		assertEquals(0, targetSeller.getPlanets().size());
		assertEquals(15000, targetSeller.getBalance());
		assertEquals(1, targetSeller.getTradeHistoryList().size());
		assertEquals(PlanetStatus.NOTFORSALE, targetPlanet.getPlanetStatus());
		assertEquals("buyer", targetPlanet.getOwner());
		assertEquals(buyer, targetPlanet.getUser());
	}

	@Test
	@DisplayName("행성구매 성공 시 거래 내역 생성")
	void test4() {
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
		UserSession userSession = new UserSession(session.getUser().getId());

		// when
		tradeService.buy(userSession, planet.getPlanetId());

		// then
		List<TradeHistory> bTradeHistory = tradeHistoryRepository.findByUserId(buyer.getId());
		List<TradeHistory> sTradeHistory = tradeHistoryRepository.findByUserId(seller.getId());

		assertEquals(1, bTradeHistory.size());
		assertEquals("buyer", bTradeHistory.get(0).getUser().getNickname());
		assertEquals("test", bTradeHistory.get(0).getPlanetName());
		assertEquals(TradeType.BUY, bTradeHistory.get(0).getTradeType());
		assertEquals(1, sTradeHistory.size());
		assertEquals("seller", sTradeHistory.get(0).getUser().getNickname());
		assertEquals("test", sTradeHistory.get(0).getPlanetName());
		assertEquals(TradeType.SELL, sTradeHistory.get(0).getTradeType());
	}

	@Test
	@DisplayName("행성구매 실패 - 잔고부족")
	void test5() {
		// given
		User buyer = User.builder()
			.email("test1@email.com")
			.password("1234")
			.nickname("buyer")
			.balance(4000)
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
		UserSession userSession = new UserSession(session.getUser().getId());

		// expected
		assertThrows(NotEnoughBalance.class,
			() -> tradeService.buy(userSession, planet.getPlanetId()));
	}
}

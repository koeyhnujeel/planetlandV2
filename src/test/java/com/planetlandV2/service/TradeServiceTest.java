package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.trade.NotEnoughBalanceException;
import com.planetlandV2.exception.trade.NotOwnerException;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.TransactionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.PlanetSell;

@SpringBootTest
class TradeServiceTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private TradeService tradeService;


	@BeforeEach
	void clean() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
		planetRepository.deleteAll();
	}

	@Test
	@DisplayName("판매등록 성공")
	void test1() {
		// given
		User user = User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
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

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(6000)
			.build();

		UserPrincipal userPrincipal = new UserPrincipal(user);
		// when
		tradeService.sell(userPrincipal, planet.getPlanetId(), planetSell);

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

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(6000)
			.build();

		UserPrincipal userPrincipal = new UserPrincipal(user);
		// expected
		assertThrows(NotOwnerException.class,
			() -> tradeService.sell(userPrincipal, planet.getPlanetId(), planetSell));
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
			.build();
		userRepository.save(buyer);

		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
			.build();
		userRepository.save(seller);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(500)
			.owner(seller.getNickname())
			.planetStatus(PlanetStatus.FORSALE)
			.build();
		planetRepository.save(planet);


		UserPrincipal principal = new UserPrincipal(buyer);
		// when
		tradeService.buy(principal, planet.getPlanetId());

		// then
		assertEquals(1, buyer.getPlanets().size());
		assertEquals("test", buyer.getPlanets().get(0).getPlanetName());
		assertEquals(500, buyer.getBalance());
		assertEquals(0, seller.getPlanets().size());
		assertEquals(1500, seller.getBalance());
		assertEquals(PlanetStatus.NOTFORSALE, planet.getPlanetStatus());
		assertEquals("buyer", planet.getOwner());
		assertEquals(buyer, planet.getUser());
		assertEquals(1, transactionRepository.findAll().size());
	}

	@Test
	@DisplayName("행성구매 성공 시 거래 내역 생성")
	void test4() {
		// given
		User buyer = User.builder()
			.email("test1@email.com")
			.password("1234")
			.nickname("buyer")
			.build();
		userRepository.save(buyer);

		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
			.build();
		userRepository.save(seller);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(500)
			.owner(seller.getNickname())
			.planetStatus(PlanetStatus.FORSALE)
			.build();
		planetRepository.save(planet);

		seller.getPlanets().add(planet);
		UserPrincipal principal = new UserPrincipal(buyer);

		// when
		tradeService.buy(principal, planet.getPlanetId());

		// then
		List<Transaction> allTransactions = transactionRepository.findAll();
		assertEquals(1, allTransactions.size());
		assertEquals("buyer", allTransactions.get(0).getBuyer().getNickname());
		assertEquals("seller", allTransactions.get(0).getSeller().getNickname());
		assertEquals("test", allTransactions.get(0).getPlanet().getPlanetName());
	}

	@Test
	@DisplayName("행성구매 실패 - 잔고부족")
	void test5() {
		// given
		User buyer = User.builder()
			.email("test1@email.com")
			.password("1234")
			.nickname("buyer")
			.build();
		userRepository.save(buyer);

		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
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
		UserPrincipal principal = new UserPrincipal(buyer);

		// expected
		assertThrows(NotEnoughBalanceException.class,
			() -> tradeService.buy(principal, planet.getPlanetId()));
	}

	@Test
	@DisplayName("행성판매 취소 성공")
	void test6() {
		// given
		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
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
		UserPrincipal principal = new UserPrincipal(seller);


		// when
		tradeService.cancel(principal, planet.getPlanetId());

		// then
		Planet targetPlanet = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(() -> new PlanetNotFound());

		assertEquals(PlanetStatus.NOTFORSALE, targetPlanet.getPlanetStatus());
	}

	@Test
	@DisplayName("행성판매 취소 실패 - 소유주가 아닐 때")
	void test7() {
		// given
		User other = User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("other")
			.build();
		userRepository.save(other);

		User seller = User.builder()
			.email("test2@email.com")
			.password("1234")
			.nickname("seller")
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
		UserPrincipal principal = new UserPrincipal(other);

		// expected
		assertThrows(NotOwnerException.class,
			() -> tradeService.cancel(principal, planet.getPlanetId()));
	}

	// @Test
	// @DisplayName("[비관적 락] 여러 명이 동시에 하나에 행성을 구매하려 할 때")
	// void test8() throws InterruptedException {
	// 	List<User> users = IntStream.range(1, 11)
	// 		.mapToObj(i -> User.builder()
	// 			.nickname("user" + i)
	// 			.password("password")
	// 			.email("test" + i + "@email.com")
	// 			.build())
	// 		.collect(Collectors.toList());
	// 	userRepository.saveAll(users);
	//
	// 	User admin = User.builder()
	// 		.email("admin@email.com")
	// 		.password("password")
	// 		.nickname("ADMIN")
	// 		.build();
	// 	userRepository.save(admin);
	//
	// 	Planet planet = Planet.builder()
	// 		.planetName("test")
	// 		.price(100)
	// 		.population(10)
	// 		.satellite(1)
	// 		.planetStatus(PlanetStatus.FORSALE)
	// 		.owner("ADMIN")
	// 		.build();
	// 	planetRepository.save(planet);
	//
	// 	final int numberOfUsers = 10;
	//
	// 	ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
	//
	// 	for (int i = 0; i < numberOfUsers; i++) {
	// 		final Long userId = (long) i + 1;
	//
	// 		executorService.execute(() -> {
	// 			UserPrincipal userPrincipal = new UserPrincipal(userRepository.findById(userId).get());
	// 			try {
	// 				tradeService.buy(userPrincipal, planet.getPlanetId());
	// 			} catch (Exception e) {
	// 				e.printStackTrace();
	// 			}
	// 		});
	// 	}
	//
	// 	executorService.shutdown();
	// 	executorService.awaitTermination(1, TimeUnit.MINUTES);
	//
	// 	List<Transaction> allTransactions = transactionRepository.findAll();
	// 	Planet target = planetRepository.findById(planet.getPlanetId())
	// 		.orElseThrow(PlanetNotFound::new);
	//
	// 	assertEquals(1, allTransactions.size());
	// 	assertEquals("미판매", target.getPlanetStatus().getValue());
	// }
}

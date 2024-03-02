package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
		planetRepository.deleteAll();
		userRepository.deleteAll();
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

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(6000)
			.build();

		UserPrincipal userPrincipal = new UserPrincipal(user);
		// when
		tradeService.sellPlanet(userPrincipal, planet.getPlanetId(), planetSell);

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

		PlanetSell planetSell = PlanetSell.builder()
			.sellPrice(6000)
			.build();

		UserPrincipal userPrincipal = new UserPrincipal(user);
		// expected
		assertThrows(NotOwnerException.class,
			() -> tradeService.sellPlanet(userPrincipal, planet.getPlanetId(), planetSell));
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
		tradeService.buyPlanet(principal, planet.getPlanetId());

		// then
		assertEquals(500, buyer.getBalance());
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

		UserPrincipal principal = new UserPrincipal(buyer);

		// when
		tradeService.buyPlanet(principal, planet.getPlanetId());

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

		UserPrincipal principal = new UserPrincipal(buyer);

		// expected
		assertThrows(NotEnoughBalanceException.class,
			() -> tradeService.buyPlanet(principal, planet.getPlanetId()));
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

		UserPrincipal principal = new UserPrincipal(seller);


		// when
		tradeService.sellCancelPlanet(principal, planet.getPlanetId());

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

		UserPrincipal principal = new UserPrincipal(other);

		// expected
		assertThrows(NotOwnerException.class,
			() -> tradeService.sellCancelPlanet(principal, planet.getPlanetId()));
	}
}

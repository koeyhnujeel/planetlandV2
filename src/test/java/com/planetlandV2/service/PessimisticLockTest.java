package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.TransactionRepository;
import com.planetlandV2.repository.UserRepository;

@SpringBootTest
public class PessimisticLockTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PlanetRepository planetRepository;

	@Autowired
	TradeService tradeService;

	@Autowired
	TransactionRepository transactionRepository;

	@BeforeEach
	void clean() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
		planetRepository.deleteAll();
	}

	@Test
	@DisplayName("[비관적 락] 여러 명이 동시에 하나에 행성을 구매하려 할 때")
	void test1() throws InterruptedException {
		List<User> users = IntStream.range(1, 11)
			.mapToObj(i -> User.builder()
				.nickname("user" + i)
				.password("password")
				.email("test" + i + "@email.com")
				.build())
			.collect(Collectors.toList());
		userRepository.saveAll(users);

		User admin = User.builder()
			.email("admin@email.com")
			.password("password")
			.nickname("ADMIN")
			.build();
		userRepository.save(admin);

		Planet planet = Planet.builder()
			.planetName("test")
			.price(100)
			.population(10)
			.satellite(1)
			.planetStatus(PlanetStatus.FORSALE)
			.owner("ADMIN")
			.build();
		planetRepository.save(planet);

		final int numberOfUsers = 10;

		ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);

		for (int i = 0; i < numberOfUsers; i++) {
			final Long userId = (long) i + 1;

			executorService.execute(() -> {
				UserPrincipal userPrincipal = new UserPrincipal(userRepository.findById(userId).get());
				try {
					tradeService.buyPlanet(userPrincipal, planet.getPlanetId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);

		List<Transaction> allTransactions = transactionRepository.findAll();
		allTransactions.forEach(System.out::println);

		Planet target = planetRepository.findById(planet.getPlanetId())
			.orElseThrow(PlanetNotFound::new);

		assertEquals(1, allTransactions.size());
		assertEquals("미판매", target.getPlanetStatus().getValue());
	}
}

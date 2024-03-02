package com.planetlandV2.Manager;

import org.springframework.stereotype.Component;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.domain.User;
import com.planetlandV2.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionManagerImpl implements TransactionManager {

	private final TransactionRepository transactionRepository;

	@Override
	public void create(User buyer, User seller, Planet planet) {
		transactionRepository.save(Transaction.builder()
			.buyer(buyer)
			.seller(seller)
			.planet(planet)
			.price(planet.getPrice())
			.savedPlanetName(planet.getPlanetName())
			.build()
		);
	}
}

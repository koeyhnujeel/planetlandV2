package com.planetlandV2.Manager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.domain.User;
import com.planetlandV2.repository.TransactionRepository;
import com.planetlandV2.request.TransactionPage;
import com.planetlandV2.response.TransactionResponse;

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

	@Override
	public List<TransactionResponse> readList(Long userId, TransactionPage transactionPage) {
		List<Transaction> transactionList = transactionRepository.getTransactionList(userId, transactionPage);
		return transactionList.stream()
			.map(Transaction::toResponse)
			.collect(Collectors.toList());
	}

	@Override
	public void removePlanet(Planet planet) {
		Optional<List<Transaction>> transactionList = transactionRepository.findByPlanet(planet);
		transactionList.ifPresent(transactions -> transactions.forEach(Transaction::deletePlanet));
	}
}

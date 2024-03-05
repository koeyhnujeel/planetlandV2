package com.planetlandV2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Manager.TransactionManager;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.TransactionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.MyPlanetPage;
import com.planetlandV2.request.TransactionPage;
import com.planetlandV2.response.PlanetResponse;
import com.planetlandV2.response.TransactionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final TransactionManager transactionManager;

	private final TransactionRepository transactionRepository;
	private final PlanetRepository planetRepository;
	private final UserRepository userRepository;

	@Transactional
	public void removeUser(Long userId) {
		Optional<List<Transaction>> userTransactions = transactionRepository.findByBuyer_idOrSeller_id(userId, userId);
		userTransactions.ifPresent(transactions -> transactions.forEach(transaction -> transaction.deleteUser(userId)));
		userRepository.deleteById(userId);
	}

	public List<TransactionResponse> findUserTransactionList(Long userId, TransactionPage transactionPage) {
		return transactionManager.readList(userId, transactionPage);
	}

	public List<PlanetResponse> findUserPlanetList(Long userId, MyPlanetPage myPlanetPage) {
		List<Planet> myPlanetsList = planetRepository.getMyPlanetList(userId, myPlanetPage);
		return myPlanetsList.stream()
			.map(Planet::toResponse)
			.collect(Collectors.toList());
	}
}

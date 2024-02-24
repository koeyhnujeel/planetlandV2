package com.planetlandV2.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.trade.AlreadyOnSaleException;
import com.planetlandV2.exception.trade.NotEnoughBalanceException;
import com.planetlandV2.exception.trade.NotForSaleException;
import com.planetlandV2.exception.trade.NotOwnerException;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.TransactionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.PlanetSell;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final UserRepository userRepository;
	private final PlanetRepository planetRepository;
	private final TransactionRepository transactionRepository;

	@Transactional
	public void sell(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId, PlanetSell planetSell) {
		User user = userRepository.findById(userPrincipal.getUserId())
			.orElseThrow(UserNotFound::new);

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(PlanetNotFound::new);
		if (planet.getPlanetStatus().equals(PlanetStatus.FORSALE)) throw new AlreadyOnSaleException();

		checkOwner(user, planet);
		planet.editPriceAndStatus(planetSell);
	}

	@Transactional
	public void buy(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId) {
		Planet planet = planetRepository.findByIdForBuy(planetId)
			.orElseThrow(PlanetNotFound::new);
		if (planet.getPlanetStatus().equals(PlanetStatus.NOTFORSALE)) throw new NotForSaleException();

		User buyer = userRepository.findById(userPrincipal.getUserId())
			.orElseThrow(UserNotFound::new);

		User seller = userRepository.findByNickname(planet.getOwner())
			.orElseThrow(UserNotFound::new);

		if (buyer.getBalance() < planet.getPrice()) {
			throw new NotEnoughBalanceException();
		}

		buyer.addPlanetAndBalanceDecrease(planet);
		seller.deletePlanetAndBalanceIncrease(planet);
		planet.changeOwnerAndStatus(buyer);

		Transaction transaction = Transaction.builder()
			.buyer(buyer)
			.seller(seller)
			.planet(planet)
			.price(planet.getPrice())
			.savedPlanetName(planet.getPlanetName())
			.build();
		transactionRepository.save(transaction);
	}

	@Transactional
	public void cancel(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId) {
		User user = userRepository.findById(userPrincipal.getUserId())
			.orElseThrow(UserNotFound::new);

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(UserNotFound::new);

		checkOwner(user, planet);
		planet.changeStatus(PlanetStatus.NOTFORSALE);
	}

	private void checkOwner(User user, Planet planet) {
		if (!user.getNickname().equals(planet.getOwner())) {
			throw new NotOwnerException();
		}
	}
}

package com.planetlandV2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.Enum.TradeType;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.trade.NotEnoughBalance;
import com.planetlandV2.exception.trade.NotOwnerException;
import com.planetlandV2.exception.planet.PlanetNotFound;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.PlanetSell;
import com.planetlandV2.response.TradeResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final UserRepository userRepository;
	private final PlanetRepository planetRepository;

	@Transactional
	public void sell(UserSession userSession, Long planetId, PlanetSell planetSell) {
		User user = userRepository.findById(userSession.id)
			.orElseThrow(() -> new UserNotFound());

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

		checkOwner(user, planet);
		planet.editPriceAndStatus(planetSell);
	}

	@Transactional
	public TradeResponse buy(UserSession userSession, Long planetId) {
		User buyer = userRepository.findById(userSession.id)
			.orElseThrow(() -> new UserNotFound());

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new PlanetNotFound());

		User seller = userRepository.findByNickname(planet.getOwner())
			.orElseThrow(() -> new UserNotFound());

		if (buyer.getBalance() < planet.getPrice()) {
			throw new NotEnoughBalance();
		}

		buyer.addPlanetAndBalanceDecrease(planet);
		seller.deletePlanetAndBalanceIncrease(planet);
		planet.changeOwnerAndStatus(buyer);

		buyer.addTradeHistory(planet, TradeType.BUY);
		seller.addTradeHistory(planet, TradeType.SELL);

		return TradeResponse.builder()
			.planetName(planet.getPlanetName())
			.price(planet.getPrice())
			.build();
	}

	@Transactional
	public void cancel(UserSession userSession, Long planetId) {
		User user = userRepository.findById(userSession.id)
			.orElseThrow(() -> new UserNotFound());

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(() -> new UserNotFound());

		checkOwner(user, planet);
		planet.changeStatus(PlanetStatus.NOTFORSALE);
	}

	private void checkOwner(User user, Planet planet) {
		if (!user.getNickname().equals(planet.getOwner())) {
			throw new NotOwnerException();
		}
	}
}

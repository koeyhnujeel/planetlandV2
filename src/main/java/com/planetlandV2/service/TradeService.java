package com.planetlandV2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.config.data.UserSession;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.NotEnoughBalance;
import com.planetlandV2.exception.NotOwnerException;
import com.planetlandV2.exception.PlanetNotFound;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.PlanetRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.PlanetSell;
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

		if (!user.getNickname().equals(planet.getOwner())) {
			throw new NotOwnerException();
		}
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

		buyer.addTradeHistory(planet, "구매");
		seller.addTradeHistory(planet, "판매");

		return TradeResponse.builder()
			.planetName(planet.getPlanetName())
			.price(planet.getPrice())
			.build();
	}
}

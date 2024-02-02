package com.planetlandV2.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.Enum.TradeType;
import com.planetlandV2.config.UserPrincipal;
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
	public void sell(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId, PlanetSell planetSell) {
		User user = userRepository.findById(userPrincipal.getUserId())
			.orElseThrow(UserNotFound::new);

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(PlanetNotFound::new);

		checkOwner(user, planet);
		planet.editPriceAndStatus(planetSell);
	}

	@Transactional
	public TradeResponse buy(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId) {
		User buyer = userRepository.findById(userPrincipal.getUserId())
			.orElseThrow(UserNotFound::new);

		Planet planet = planetRepository.findById(planetId)
			.orElseThrow(PlanetNotFound::new);

		User seller = userRepository.findByNickname(planet.getOwner())
			.orElseThrow(UserNotFound::new);

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

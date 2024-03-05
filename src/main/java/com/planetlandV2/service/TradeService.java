package com.planetlandV2.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.Manager.TradeManager;
import com.planetlandV2.Manager.TransactionManager;
import com.planetlandV2.Reader.PlanetReader;
import com.planetlandV2.Reader.UserReader;
import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.trade.AlreadyOnSaleException;
import com.planetlandV2.exception.trade.NotEnoughBalanceException;
import com.planetlandV2.exception.trade.NotForSaleException;
import com.planetlandV2.exception.trade.NotOwnerException;
import com.planetlandV2.request.PlanetSell;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final UserReader userReader;
	private final PlanetReader planetReader;
	private final TradeManager tradeManager;
	private final TransactionManager transactionManager;

	@Transactional
	public void sellPlanet(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId, PlanetSell planetSell) {

		User user = userReader.read(userPrincipal.getUserId());
		Planet planet = planetReader.read(planetId);

		checkNotForSale(planet.getPlanetStatus());
		checkOwner(user, planet);
		tradeManager.sell(planet, planetSell);
	}

	@Transactional
	public void buyPlanet(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId) {

		Planet planet = planetReader.read(planetId);
		checkForSale(planet.getPlanetStatus());

		User buyer = userReader.read(userPrincipal.getUserId());
		checkBalance(buyer.getBalance(), planet.getPrice());

		User seller = userReader.read(planet.getOwner());

		tradeManager.deal(buyer, seller, planet);
		transactionManager.create(buyer, seller, planet);
	}

	@Transactional
	public void sellCancelPlanet(@AuthenticationPrincipal UserPrincipal userPrincipal, Long planetId) {

		User user = userReader.read(userPrincipal.getUserId());
		Planet planet = planetReader.read(planetId);

		checkOwner(user, planet);
		tradeManager.cancel(planet);
	}

	private void checkOwner(User user, Planet planet) {
		if (!user.getNickname().equals(planet.getOwner())) {
			throw new NotOwnerException();
		}
	}

	private void checkForSale(PlanetStatus planetStatus) {
		if (planetStatus.equals(PlanetStatus.NOTFORSALE)) {
			throw new NotForSaleException();
		}
	}

	private void checkNotForSale(PlanetStatus planetStatus) {
		if (planetStatus.equals(PlanetStatus.FORSALE)) {
			throw new AlreadyOnSaleException();
		}
	}

	private void checkBalance(Integer balance, Integer price) {
		if (balance < price) {
			throw new NotEnoughBalanceException();
		}
	}
}

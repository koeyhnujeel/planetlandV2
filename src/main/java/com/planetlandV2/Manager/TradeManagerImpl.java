package com.planetlandV2.Manager;

import org.springframework.stereotype.Component;

import com.planetlandV2.Enum.PlanetStatus;
import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;
import com.planetlandV2.request.PlanetSell;

@Component
public class TradeManagerImpl implements TradeManager {

	@Override
	public void sell(Planet planet, PlanetSell planetSell) {
		planet.salesSetting(planetSell);
	}

	@Override
	public void deal(User buyer, User seller, Planet planet) {
		buyer.decreaseBalance(planet);
		seller.increaseBalance(planet);
		planet.changeOwnerAndStatus(buyer);
	}

	@Override
	public void cancel(Planet planet) {
		planet.cancelSale(PlanetStatus.NOTFORSALE);
	}
}

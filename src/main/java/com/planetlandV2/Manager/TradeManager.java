package com.planetlandV2.Manager;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;
import com.planetlandV2.request.PlanetSell;

public interface TradeManager {
	void deal(User buyer, User seller, Planet planet);

	void sell(Planet planet, PlanetSell planetSell);

	void cancel(Planet planet);
}

package com.planetlandV2.Manager;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;

public interface TransactionManager {

	void create(User buyer, User seller, Planet planet);
}

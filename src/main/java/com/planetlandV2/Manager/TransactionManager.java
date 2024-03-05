package com.planetlandV2.Manager;

import java.util.List;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.User;
import com.planetlandV2.request.TransactionPage;
import com.planetlandV2.response.TransactionResponse;

public interface TransactionManager {

	void create(User buyer, User seller, Planet planet);

	void removePlanet(Planet planet);

	List<TransactionResponse> readList(Long userId, TransactionPage transactionPage);
}

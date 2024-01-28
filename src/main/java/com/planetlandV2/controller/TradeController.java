package com.planetlandV2.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.planetlandV2.request.PlanetSell;
import com.planetlandV2.response.TradeResponse;
import com.planetlandV2.service.TradeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TradeController {

	private final TradeService tradeService;

	@PatchMapping("/planets/{planetId}/sell")
	public void salesRegistration(UserSession userSession, @PathVariable Long planetId, @RequestBody @Valid PlanetSell planetSell) {
		tradeService.sell(userSession, planetId, planetSell);
	}

	@PatchMapping("/planets/{planetId}/buy")
	public TradeResponse buy(UserSession userSession, @PathVariable Long planetId) {
		return tradeService.buy(userSession, planetId);
	}

	@PatchMapping("/planets/{planetId}/sellCancel")
	public void sellCancel(UserSession userSession, @PathVariable Long planetId) {
		tradeService.cancel(userSession, planetId);
	}
}

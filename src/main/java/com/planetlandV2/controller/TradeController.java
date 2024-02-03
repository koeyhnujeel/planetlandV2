package com.planetlandV2.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.request.PlanetSell;
import com.planetlandV2.response.TradeResponse;
import com.planetlandV2.service.TradeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TradeController {

	private final TradeService tradeService;

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@PatchMapping("/planets/{planetId}/sell")
	public void salesRegistration(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long planetId, @RequestBody @Valid PlanetSell planetSell) {
		tradeService.sell(userPrincipal, planetId, planetSell);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@PatchMapping("/planets/{planetId}/buy")
	public TradeResponse buy(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long planetId) {
		return tradeService.buy(userPrincipal, planetId);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@PatchMapping("/planets/{planetId}/sellCancel")
	public void sellCancel(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long planetId) {
		tradeService.cancel(userPrincipal, planetId);
	}
}

package com.planetlandV2.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.request.MyPlanetPage;
import com.planetlandV2.request.TransactionPage;
import com.planetlandV2.response.PlanetResponse;
import com.planetlandV2.response.TransactionResponse;
import com.planetlandV2.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@DeleteMapping("/user/withdrawal")
	public void userRemove(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		userService.removeUser(userPrincipal.getUserId());
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping("/user/transactions")
	public List<TransactionResponse> userTransactionList(
		@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute TransactionPage transactionPage) {
		return userService.findUserTransactionList(userPrincipal.getUserId(), transactionPage);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping("/user/planets")
	public List<PlanetResponse> userPlanetList(
		@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute MyPlanetPage myPlanetPage) {
		return userService.findUserPlanetList(userPrincipal.getUserId(), myPlanetPage);
	}
}

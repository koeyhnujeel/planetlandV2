package com.planetlandV2.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.planetlandV2.Enum.TradeType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String password;

	private String nickname;

	private Integer balance;

	private LocalDateTime createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Session> sessions = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Planet> planets = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<TradeHistory> tradeHistoryList = new ArrayList<>();

	@Builder
	public User(String email, String password, String nickname, Integer balance) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.balance = balance;
		this.createdAt = LocalDateTime.now();
	}

	public Session addSession() {
		Session session = Session.builder()
			.user(this)
			.build();
		sessions.add(session);

		return session;
	}

	public void addPlanetAndBalanceDecrease(Planet planet) {
		this.planets.add(planet);
		this.balance -= planet.getPrice();
	}

	public void deletePlanetAndBalanceIncrease(Planet planet) {
		this.planets.remove(planet);
		this.balance += planet.getPrice();
	}

	public void addTradeHistory(Planet planet, TradeType tradeType) {
		TradeHistory tradeHistory = TradeHistory.builder()
			.planetName(planet.getPlanetName())
			.tradeType(tradeType)
			.price(planet.getPrice())
			.user(this)
			.build();
		this.tradeHistoryList.add(tradeHistory);
	}
}

package com.planetlandV2.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.planetlandV2.constant.Balance;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private List<Planet> planets = new ArrayList<>();

	@Builder
	public User(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.balance = Balance.INITIAL_CAPITAL;
		this.createdAt = LocalDateTime.now();
	}

	public void addPlanetAndBalanceDecrease(Planet planet) {
		this.planets.add(planet);
		this.balance -= planet.getPrice();
	}

	public void deletePlanetAndBalanceIncrease(Planet planet) {
		this.planets.remove(planet);
		this.balance += planet.getPrice();
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", email='" + email + '\'' +
			", password='" + password + '\'' +
			", nickname='" + nickname + '\'' +
			", balance=" + balance +
			", createdAt=" + createdAt +
			'}';
	}
}

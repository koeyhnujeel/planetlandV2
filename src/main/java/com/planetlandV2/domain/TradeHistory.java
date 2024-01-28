package com.planetlandV2.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import com.planetlandV2.Enum.TradeType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String planetName;
	private TradeType tradeType;
	private Integer price;
	private LocalDateTime createdAt;

	@ManyToOne
	private User user;

	@Builder
	public TradeHistory(String planetName, TradeType tradeType, Integer price, User user) {
		this.planetName = planetName;
		this.tradeType = tradeType;
		this.price = price;
		this.user = user;
		this.createdAt = LocalDateTime.now();
	}
}

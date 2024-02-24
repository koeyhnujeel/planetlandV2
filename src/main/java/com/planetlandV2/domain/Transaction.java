package com.planetlandV2.domain;

import java.time.LocalDateTime;

import com.planetlandV2.response.TransactionResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User buyer;

	@ManyToOne
	private User seller;

	@ManyToOne
	private Planet planet;

	private Integer price;

	private LocalDateTime transactionDate;

	private boolean isBuyerWithdrawal;

	private boolean isSellerWithdrawal;

	private String savedPlanetName;

	@Builder
	public Transaction(User buyer, User seller, Planet planet, Integer price, boolean isBuyerWithdrawal, boolean isSellerWithdrawal, String savedPlanetName) {
		this.buyer = buyer;
		this.seller = seller;
		this.planet = planet;
		this.price = price;
		this.transactionDate = LocalDateTime.now();
		this.isBuyerWithdrawal = false;
		this.isSellerWithdrawal = false;
		this.savedPlanetName = savedPlanetName;
	}

	public TransactionResponse toResponse() {
		return TransactionResponse.builder()
		   .buyer(this.isBuyerWithdrawal ? "탈퇴한 회원" : this.buyer.getNickname())
		   .seller(this.isSellerWithdrawal ? "탈퇴한 회원" : this.seller.getNickname())
		   .planet(this.planet != null ? this.planet.getPlanetName() : this.savedPlanetName)
		   .price(this.price)
		   .transactionDate(this.transactionDate)
		   .build();
	}

	public void deleteUser(Long userId) {
		if (this.buyer.getId().equals(userId)) {
			this.buyer = null;
			this.isBuyerWithdrawal = true;
		} else if (this.seller.getId().equals(userId)) {
			this.seller = null;
			this.isSellerWithdrawal = true;
		}
	}

	public void deletePlanet() {
		this.planet = null;
	}

	@Override
	public String toString() {
		return "Transaction{" +
			"id=" + id +
			", buyer=" + buyer +
			", seller=" + seller +
			", planet=" + planet +
			", price=" + price +
			", transactionDate=" + transactionDate +
			", savedPlanetName='" + savedPlanetName + '\'' +
			'}';
	}
}

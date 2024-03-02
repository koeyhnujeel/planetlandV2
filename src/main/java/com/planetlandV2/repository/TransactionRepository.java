package com.planetlandV2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.Planet;
import com.planetlandV2.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {

	Optional<List<Transaction>> findByPlanet(Planet planet);

	Optional<List<Transaction>> findByBuyer_idOrSeller_id(Long buyerId, Long sellerId);

	@Modifying
	@Query("DELETE FROM Transaction t WHERE t.isBuyerWithdrawal = TRUE AND t.isSellerWithdrawal = TRUE")
	void deleteAllByTransactionsWithdrawals();
}

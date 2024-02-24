package com.planetlandV2.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {

	Optional<List<Transaction>> findByPlanet_id(Long planetId);

	Optional<List<Transaction>> findByBuyer_idOrSeller_id(Long buyerId, Long sellerId);

	void deleteAllByTransactionDateBefore(LocalDateTime date);
}

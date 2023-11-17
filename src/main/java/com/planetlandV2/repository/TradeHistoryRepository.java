package com.planetlandV2.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.TradeHistory;

@Repository
public interface TradeHistoryRepository extends CrudRepository<TradeHistory, Long> {
	List<TradeHistory> findByUserId(Long userId);
}

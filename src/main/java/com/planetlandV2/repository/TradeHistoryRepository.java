package com.planetlandV2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.TradeHistory;

@Repository
public interface TradeHistoryRepository extends CrudRepository<TradeHistory, Long> {
}

package com.planetlandV2.service;


import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.planetlandV2.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;

	@Scheduled(cron = "0 0 0 1 * ?")
	public void deleteOldTransactions() {
		transactionRepository.deleteAllByTransactionDateBefore(LocalDateTime.now().minusYears(2));
	}
}

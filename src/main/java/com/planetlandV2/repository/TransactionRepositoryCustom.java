package com.planetlandV2.repository;

import java.util.List;

import com.planetlandV2.domain.Transaction;
import com.planetlandV2.request.TransactionPage;

public interface TransactionRepositoryCustom {

	List<Transaction> getTransactionList(Long userId, TransactionPage transactionPage);
}

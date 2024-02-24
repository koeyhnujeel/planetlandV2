package com.planetlandV2.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.planetlandV2.Enum.TransactionType;
import com.planetlandV2.domain.QTransaction;
import com.planetlandV2.domain.Transaction;
import com.planetlandV2.request.TransactionPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Transaction> getTransactionList(Long userId, TransactionPage transactionPage) {
		QTransaction transaction = QTransaction.transaction;

		Map<TransactionType, BooleanExpression> type = new HashMap<>(Map.of(
			TransactionType.ALL, transaction.buyer.id.eq(userId).or(transaction.seller.id.eq(userId)),
			TransactionType.BUY, transaction.buyer.id.eq(userId),
			TransactionType.SELL, transaction.seller.id.eq(userId)
		));

		return jpaQueryFactory
			.selectFrom(transaction)
			.where(type.get(transactionPage.getKeyword()))
			.limit(transactionPage.getSize())
			.offset(transactionPage.getOffset())
			.orderBy(transaction.transactionDate.desc())
			.fetch();
	}
}

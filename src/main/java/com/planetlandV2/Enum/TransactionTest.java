package com.planetlandV2.Enum;

import java.util.function.Function;

import com.planetlandV2.domain.QTransaction;
import com.querydsl.core.types.dsl.BooleanExpression;

public enum TransactionTest {
	ALL(value -> QTransaction.transaction.buyer.id.eq(value).or(QTransaction.transaction.seller.id.eq(value))),
	BUY(value -> QTransaction.transaction.buyer.id.eq(value)),
	SELL(value -> QTransaction.transaction.seller.id.eq(value));

	private Function<Long, BooleanExpression> expression;

	TransactionTest(Function<Long, BooleanExpression> expression) {
		this.expression = expression;
	}

	public BooleanExpression getResult(Long userId) {
		return this.expression.apply(userId);
	}
}

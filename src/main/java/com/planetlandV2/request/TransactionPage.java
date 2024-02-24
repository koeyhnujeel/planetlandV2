package com.planetlandV2.request;

import static java.lang.Math.*;

import com.planetlandV2.Enum.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionPage {

	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 20;

	@Builder.Default
	private TransactionType keyword = TransactionType.ALL;

	public long getOffset() {
		return (long) (max(1, page) - 1) * max(20, size);
	}
}

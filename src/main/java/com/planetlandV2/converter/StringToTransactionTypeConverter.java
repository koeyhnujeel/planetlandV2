package com.planetlandV2.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.planetlandV2.Enum.TransactionType;

@Component
public class StringToTransactionTypeConverter implements Converter<String, TransactionType> {

	@Override
	public TransactionType convert(String source) {
		return TransactionType.from(source);
	}
}

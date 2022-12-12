package com.hamersztein.efxtest.model;

public interface CurrencyPairPriceCache {
	SpotPrice getLatestPriceForCurrencyPair(final String currencyPair);
}

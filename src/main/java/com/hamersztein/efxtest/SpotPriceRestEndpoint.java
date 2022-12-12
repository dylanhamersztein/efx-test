package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.CurrencyPairPriceCache;
import com.hamersztein.efxtest.model.SpotPrice;
import lombok.RequiredArgsConstructor;

// This represents the REST endpoint which I would likely create with Spring.
@RequiredArgsConstructor
public class SpotPriceRestEndpoint {

	private final CurrencyPairPriceCache currencyPairPriceCache;

	public SpotPrice getLatestPriceForCurrencyPair(final String currencyPair) {
		return currencyPairPriceCache.getLatestPriceForCurrencyPair(currencyPair);
	}
}

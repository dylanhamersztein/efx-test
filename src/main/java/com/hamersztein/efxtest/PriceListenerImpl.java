package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.CurrencyPairPriceCache;
import com.hamersztein.efxtest.model.PriceListener;
import com.hamersztein.efxtest.model.SpotPrice;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class PriceListenerImpl implements PriceListener, CurrencyPairPriceCache {

	private final Map<String, SpotPrice> latestPriceForCurrencyPair = new ConcurrentHashMap<>();

	private final SpotPriceProcessor spotPriceProcessor;

	private final SpotPriceFactory spotPriceFactory;

	@Override
	public void onMessage(final String message) {
		spotPriceFactory.fromStringMessage(message)
				.map(spotPriceProcessor::applyMargin)
				.forEach(adjustedPrice -> latestPriceForCurrencyPair.put(adjustedPrice.getCurrencyPair(), adjustedPrice));
	}

	@Override
	public SpotPrice getLatestPriceForCurrencyPair(final String currencyPair) {
		return latestPriceForCurrencyPair.get(currencyPair.trim());
	}
}

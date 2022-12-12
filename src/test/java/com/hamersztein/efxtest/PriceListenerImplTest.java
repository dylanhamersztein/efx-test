package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.SpotPrice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PriceListenerImplTest {

	private final SpotPriceProcessor spotPriceProcessor = mock(SpotPriceProcessor.class);

	private final SpotPriceFactory spotPriceFactory = mock(SpotPriceFactory.class);

	private final PriceListenerImpl priceListener = new PriceListenerImpl(spotPriceProcessor, spotPriceFactory);

	@Test
	void shouldCorrectlyCachePriceForCurrencyPair() {
		final String message = "110, EUR/JPY, 1, 1,01-06-2020 12:01:02:110";
		final SpotPrice expectedSpotPrice = new SpotPrice(1, "EUR/JPY", ONE, ONE, LocalDateTime.now().minusSeconds(5));
		final SpotPrice adjustedSpotPrice = new SpotPrice(1, "EUR/JPY", BigDecimal.valueOf(0.99), BigDecimal.valueOf(1.01), LocalDateTime.now().minusSeconds(5));


		when(spotPriceFactory.fromStringMessage(eq(message)))
				.thenReturn(Stream.of(expectedSpotPrice));

		when(spotPriceProcessor.applyMargin(eq(expectedSpotPrice)))
				.thenReturn(adjustedSpotPrice);

		priceListener.onMessage(message);

		final SpotPrice cachedPrice = priceListener.getLatestPriceForCurrencyPair("EUR/JPY");

		assertNotNull(cachedPrice);
		assertEquals(adjustedSpotPrice, cachedPrice);
	}

	@Test
	void shouldUpdateCachedPriceForCurrencyPairWhenNewMessageComesIn() {
		final String message = "110, EUR/JPY, 1, 1,01-06-2020 12:01:02:110";
		final SpotPrice firstExpectedSpotPrice = new SpotPrice(110, "EUR/JPY", ONE, ONE, LocalDateTime.now().minusSeconds(5));
		final SpotPrice firstAdjustedSpotPrice = new SpotPrice(110, "EUR/JPY", BigDecimal.valueOf(0.99), BigDecimal.valueOf(1.01), LocalDateTime.now().minusSeconds(5));

		final String newMessage = "111, EUR/JPY, 1, 1,01-06-2020 12:01:02:115";
		final SpotPrice newExpectedSpotPrice = new SpotPrice(111, "EUR/JPY", ONE, ONE, LocalDateTime.now().minusSeconds(5));
		final SpotPrice newAdjustedSpotPrice = new SpotPrice(111, "EUR/JPY", BigDecimal.valueOf(0.99), BigDecimal.valueOf(1.01), LocalDateTime.now().minusSeconds(5));

		when(spotPriceFactory.fromStringMessage(any())).thenAnswer(answer -> {
			final String stringInput = answer.getArgument(0);

			if (stringInput.contains("110")) {
				return Stream.of(firstExpectedSpotPrice);
			} else {
				return Stream.of(newExpectedSpotPrice);
			}
		});

		when(spotPriceProcessor.applyMargin(any())).thenAnswer(answer -> {
			final SpotPrice input = answer.getArgument(0);

			if (input.getId() == 110) {
				return firstAdjustedSpotPrice;
			} else {
				return newAdjustedSpotPrice;
			}
		});

		priceListener.onMessage(message);

		final SpotPrice cachedPrice = priceListener.getLatestPriceForCurrencyPair("EUR/JPY");

		assertNotNull(cachedPrice);
		assertEquals("EUR/JPY", cachedPrice.getCurrencyPair());
		assertEquals(110, cachedPrice.getId());

		priceListener.onMessage(newMessage);

		final SpotPrice newCachedPrice = priceListener.getLatestPriceForCurrencyPair("EUR/JPY");

		assertEquals("EUR/JPY", newCachedPrice.getCurrencyPair());
		assertEquals(111, newCachedPrice.getId());
		assertTrue(newCachedPrice.getTimestamp().isAfter(cachedPrice.getTimestamp()));
	}

}
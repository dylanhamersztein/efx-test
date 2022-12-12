package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.SpotPrice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SpotPriceProcessorTest {

	private final SpotPriceProcessor spotPriceProcessor = new SpotPriceProcessor();

	@Test
	void shouldApplyMarginToBothSides() {
		SpotPrice spotPrice = spotPriceProcessor.applyMargin(
				new SpotPrice(1, "EUR/JPY", ONE, ONE, LocalDateTime.now())
		);

		assertEquals(BigDecimal.valueOf(0.99), spotPrice.getBid());
		assertEquals(BigDecimal.valueOf(1.01), spotPrice.getAsk());
	}

}
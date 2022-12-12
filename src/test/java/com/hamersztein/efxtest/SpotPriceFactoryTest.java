package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.SpotPrice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpotPriceFactoryTest {

	private final SpotPriceFactory spotPriceFactory = new SpotPriceFactory();

	@Test
	void shouldTurnSingleLineMessageIntoSingleSpotPrice() {
		final String message = "110, EUR/JPY, 119.61, 119.91,01-06-2020 12:01:02:110";
		List<SpotPrice> spotPrices = spotPriceFactory.fromStringMessage(message).collect(Collectors.toList());

		assertEquals(1, spotPrices.size());

		final SpotPrice spotPrice = spotPrices.get(0);
		validateSpotPrice(spotPrice);
	}

	@Test
	void shouldTurnMultiLineMessageIntoManySpotPrices() {
		final String multiLineMessage = IntStream.range(1, 101)
				.mapToObj(index -> index + ", EUR/JPY, 119.61, 119.91,01-06-2020 12:01:02:110")
				.collect(Collectors.joining("\n"));

		List<SpotPrice> spotPrices = spotPriceFactory.fromStringMessage(multiLineMessage).collect(Collectors.toList());
		assertEquals(100, spotPrices.size());

		spotPrices.forEach(this::validateSpotPrice);
	}

	private void validateSpotPrice(final SpotPrice spotPrice) {
		assertEquals("EUR/JPY", spotPrice.getCurrencyPair());
		assertEquals(BigDecimal.valueOf(119.61), spotPrice.getBid());
		assertEquals(BigDecimal.valueOf(119.91), spotPrice.getAsk());
		assertEquals(LocalDateTime.parse("01-06-2020 12:01:02:110", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")), spotPrice.getTimestamp());
	}

}
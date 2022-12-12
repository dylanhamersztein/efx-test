package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.SpotPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

public class SpotPriceFactory {

	private final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss:SSS";

	public Stream<SpotPrice> fromStringMessage(final String messages) {
		return Arrays.stream(messages.split("\n"))
				.map(String::trim)
				.map(messageLine -> {
					final String[] messageParts = messageLine.split(",");

					final int id = Integer.parseInt(messageParts[0].trim());
					final String currencyPair = messageParts[1].trim();
					final BigDecimal bid = new BigDecimal(messageParts[2].trim());
					final BigDecimal ask = new BigDecimal(messageParts[3].trim());
					final LocalDateTime timestamp = LocalDateTime.parse(messageParts[4].trim(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));

					return new SpotPrice(id, currencyPair, bid, ask, timestamp);
				});
	}
}

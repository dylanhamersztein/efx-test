package com.hamersztein.efxtest;

import com.hamersztein.efxtest.model.SpotPrice;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

public class SpotPriceProcessor {

	private final BigDecimal BID_MARGIN = ONE.subtract(BigDecimal.valueOf(0.01));
	private final BigDecimal ASK_MARGIN = ONE.add(BigDecimal.valueOf(0.01));

	public SpotPrice applyMargin(final SpotPrice spotPrice) {
		spotPrice.setBid(spotPrice.getBid().multiply(BID_MARGIN));
		spotPrice.setAsk(spotPrice.getAsk().multiply(ASK_MARGIN));

		return spotPrice;
	}
}

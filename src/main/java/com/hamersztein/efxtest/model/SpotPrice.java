package com.hamersztein.efxtest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class SpotPrice {

    private int id;

    private String currencyPair;

    private BigDecimal bid;

    private BigDecimal ask;

    private LocalDateTime timestamp;
}

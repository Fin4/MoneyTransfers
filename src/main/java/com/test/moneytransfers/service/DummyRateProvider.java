package com.test.moneytransfers.service;

import java.math.BigDecimal;
import java.util.Currency;

public class DummyRateProvider implements RateProvider {

    private static final BigDecimal rate = new BigDecimal("1.00");

    @Override
    public BigDecimal getRate(Currency from, Currency to) {
        return rate;
    }
}

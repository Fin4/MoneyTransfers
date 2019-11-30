package com.test.moneytransfers.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;

public class Transfer {

    private String id;

    private String accNumberFrom;

    private String accNumberTo;

    private BigDecimal amount;

    private BigDecimal rate;

    private Currency currency;

    private Instant timestamp;

    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccNumberFrom() {
        return accNumberFrom;
    }

    public void setAccNumberFrom(String accNumberFrom) {
        this.accNumberFrom = accNumberFrom;
    }

    public String getAccNumberTo() {
        return accNumberTo;
    }

    public void setAccNumberTo(String accNumberTo) {
        this.accNumberTo = accNumberTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(accNumberFrom, transfer.accNumberFrom) &&
                Objects.equals(accNumberTo, transfer.accNumberTo) &&
                Objects.equals(timestamp, transfer.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accNumberFrom, accNumberTo, timestamp);
    }
}

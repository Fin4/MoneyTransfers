package com.test.moneytransfers.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;

public class Transfer {

    private Long id;

    private Long senderAccId;

    private Long receiverAccId;

    private BigDecimal amount;

    private BigDecimal rate;

    private Currency currency;

    private Instant timestamp;

    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderAccId() {
        return senderAccId;
    }

    public void setSenderAccId(Long senderAccId) {
        this.senderAccId = senderAccId;
    }

    public Long getReceiverAccId() {
        return receiverAccId;
    }

    public void setReceiverAccId(Long receiverAccId) {
        this.receiverAccId = receiverAccId;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transfer transfer = (Transfer) o;
        return Objects.equals(senderAccId, transfer.senderAccId) &&
            Objects.equals(receiverAccId, transfer.receiverAccId) &&
            Objects.equals(timestamp, transfer.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderAccId, receiverAccId, timestamp);
    }
}

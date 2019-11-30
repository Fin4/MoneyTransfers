package com.test.moneytransfers.dto;

public class AccountPostRequetDto {

    private String currency;
    private String balance;

    public AccountPostRequetDto(String currency, String balance) {
        this.currency = currency;
        this.balance = balance;
    }

    public AccountPostRequetDto() {}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

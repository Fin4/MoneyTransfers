package com.test.moneytransfers.dto;

import com.test.moneytransfers.model.Account;

public class AccountDto {

    public Long id;
    public String number;
    public String balance;
    public String currency;

    public static AccountDto from(Account account) {
        AccountDto accountDto = new AccountDto();

        accountDto.id = account.getId();
        accountDto.balance = account.getBalance().toString();
        accountDto.number = account.getNumber();
        accountDto.currency = account.getCurrency().getCurrencyCode();

        return accountDto;
    }

}

package com.test.moneytransfers.service;

import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.model.Account;
import com.test.moneytransfers.model.Transfer;

import java.util.Collection;

public interface MoneyTransferService {

    Collection<Account> listAccounts();

    Account getById(Long id);

    Account create(AccountPostRequetDto accDto);

    Account delete(Long id);

    Transfer transferMoney(long senderAccId, long receiverAccId, String amount);

    Collection<Transfer> listTransfers();
}

package com.test.moneytransfers.service;

import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.model.Account;

import java.util.Collection;

public interface AccountService {

    Collection<Account> getAll();

    Account getById(Long id);

    Account create(AccountPostRequetDto accDto);

    Account delete(Long id);
}

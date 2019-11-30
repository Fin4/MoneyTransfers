package com.test.moneytransfers.service;

import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.errors.NotFoundException;
import com.test.moneytransfers.model.Account;
import com.test.moneytransfers.model.Transfer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountServiceImpl implements AccountService {

    private static final ConcurrentHashMap<Long, Account> accounts = new ConcurrentHashMap<>();

    private final AtomicLong accNumProvider = new AtomicLong();

    @Override
    public Collection<Account> getAll() {
        return accounts.values();
    }

    public Account getById(Long id) {
        return Optional.ofNullable(accounts.get(id))
                .orElseThrow(() -> new NotFoundException("Account with given id does not exist"));
    }

    public Account create(AccountPostRequetDto accDto) {
        var account = new Account();

        long num = accNumProvider.incrementAndGet();

        account.setId(num);
        account.setNumber(String.format("%08d", num));
        account.setCurrency(Currency.getInstance(accDto.getCurrency()));
        account.setBalance(new BigDecimal(accDto.getBalance()));

        accounts.put(account.getId(), account);

        return account;
    }

    @Override
    public Account delete(Long id) {
        return Optional.ofNullable(accounts.remove(id))
                .orElseThrow(() -> new NotFoundException("Account with given id does not exist"));
    }

    public Transfer transferMoney(long senderAccId, long recieverAccId, String amount) {

        return new Transfer();
    }

    private static class AccWrapper {

        Account account;
        private final ReadWriteLock lock;

        public AccWrapper(Account account) {
            this.account = account;
            this.lock = new ReentrantReadWriteLock();
        }

        public ReadWriteLock getLock() {
            return lock;
        }
    }
}

package com.test.moneytransfers.service;

import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.errors.NotFoundException;
import com.test.moneytransfers.errors.TransferException;
import com.test.moneytransfers.model.Account;
import com.test.moneytransfers.model.Transfer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class InMemoryMoneyTransferService implements MoneyTransferService {

    private final ConcurrentHashMap<Long, AccWrapper> accounts = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, Transfer> transfers = new ConcurrentHashMap<>();

    private final AtomicLong accIdProvider = new AtomicLong();

    private final AtomicLong transferIdProvider = new AtomicLong();

    private final ReentrantLock transferLock = new ReentrantLock();

    @Override
    public Collection<Account> listAccounts() {
        return accounts.values().stream()
                       .map(AccWrapper::getAccount)
                       .collect(Collectors.toList());
    }

    public Account getById(Long id) {

        var accWrapper = Optional.ofNullable(accounts.get(id))
                                 .orElseThrow(() -> new NotFoundException("Account with given id does not exist"));

        try {
            accWrapper.lock.readLock().lock();
            return accWrapper.account;
        } finally {
            accWrapper.lock.readLock().unlock();
        }
    }

    public Account create(AccountPostRequetDto accDto) {
        var account = new Account();

        long num = accIdProvider.incrementAndGet();

        account.setId(num);
        account.setNumber(String.format("%08d", num));
        account.setCurrency(Currency.getInstance(accDto.getCurrency()));
        account.setBalance(new BigDecimal(accDto.getBalance()));

        accounts.put(account.getId(), new AccWrapper(account));

        return account;
    }

    @Override
    public Account delete(Long id) {

      var accWrapper = Optional.ofNullable(accounts.get(id))
                                      .orElseThrow(() -> new NotFoundException("Account with given id does not exist"));

      try {
        accWrapper.lock.writeLock().lock();
        return accounts.remove(id).account;
      } finally {
        accWrapper.lock.writeLock().unlock();
      }
    }

    public Transfer transferMoney(long senderAccId, long receiverAccId, String amount) {

        var senderAccWrapper = accounts.get(senderAccId);
        var receiverAccWrapper = accounts.get(receiverAccId);

        var bigDecimalAmount = new BigDecimal(amount);

        if (senderAccWrapper.account.getBalance().compareTo(bigDecimalAmount) < 0)
            throw new TransferException("Transfer not possible, insufficient funds");

        //bottleneck
        transferLock.lock();
        try {
            senderAccWrapper.lock.writeLock().lock();
            receiverAccWrapper.lock.writeLock().lock();
        } finally {
            transferLock.unlock();
        }

        try {
            senderAccWrapper.account.setBalance(
                senderAccWrapper.account.getBalance().subtract(bigDecimalAmount));

            receiverAccWrapper.account.setBalance(
                receiverAccWrapper.account.getBalance().add(bigDecimalAmount));

            var transfer = new Transfer();
            transfer.setId(transferIdProvider.incrementAndGet());
            transfer.setReceiverAccId(receiverAccId);
            transfer.setSenderAccId(senderAccId);
            transfer.setAmount(bigDecimalAmount);
            transfer.setCurrency(senderAccWrapper.account.getCurrency());
            transfer.setRate(new BigDecimal("1.00"));
            transfer.setTimestamp(Instant.now());
            transfer.setNotes("Some notes");

            transfers.put(transfer.getId(), transfer);

            return transfer;

        } finally {
            senderAccWrapper.lock.writeLock().unlock();
            receiverAccWrapper.lock.writeLock().unlock();
        }
    }

    @Override
    public Collection<Transfer> listTransfers() {
        return transfers.values();
    }

    private class AccWrapper {

        private final Account account;
        private final ReadWriteLock lock;

        public AccWrapper(Account account) {
            this.account = account;
            this.lock = new ReentrantReadWriteLock();
        }

        public Account getAccount() {
            return account;
        }
    }
}
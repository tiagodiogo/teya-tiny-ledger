package com.tiagodiogo.tiny_ledger.repository;

import com.tiagodiogo.tiny_ledger.domain.entity.CustomerAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LedgerRepository {
  private static final LedgerRepository INSTANCE = new LedgerRepository();
  private final Map<UUID, CustomerAccount> accounts = new HashMap<>();

  private LedgerRepository() {
  }

  public static LedgerRepository getInstance() {
    return INSTANCE;
  }

  public synchronized void storeCustomerAccount(final UUID customerId, final CustomerAccount account) {
    accounts.put(customerId, account);
  }

  public Optional<CustomerAccount> getCustomerAccount(final UUID customerId) {
    return Optional.ofNullable(accounts.get(customerId));
  }

}

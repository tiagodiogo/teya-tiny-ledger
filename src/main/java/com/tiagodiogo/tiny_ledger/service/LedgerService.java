package com.tiagodiogo.tiny_ledger.service;

import com.tiagodiogo.tiny_ledger.domain.dto.CreateTransactionDTO;
import com.tiagodiogo.tiny_ledger.domain.dto.CustomerDTO;
import com.tiagodiogo.tiny_ledger.domain.dto.TransactionDTO;
import com.tiagodiogo.tiny_ledger.domain.entity.CustomerAccount;
import com.tiagodiogo.tiny_ledger.domain.entity.Transaction;
import com.tiagodiogo.tiny_ledger.repository.LedgerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LedgerService {

  public static final String CUSTOMER_ACCOUNT_NOT_FOUND = "Customer account not found";
  public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
  public static final String INVALID_TRANSACTION_TYPE = "Invalid transaction type";

  private final LedgerRepository ledgerRepository = LedgerRepository.getInstance();

  public CustomerDTO createCustomerAccount() {
    final UUID customerId = UUID.randomUUID();
    final CustomerAccount customerAccount = CustomerAccount.builder()
        .id(customerId)
        .balance(BigDecimal.ZERO)
        .transactions(new ArrayList<>())
        .build();
    ledgerRepository.storeCustomerAccount(customerId, customerAccount);
    log.info("Created Customer Account: {}", customerId);
    return CustomerDTO.builder().id(customerId).build();
  }

  public BigDecimal getCustomerBalance(final UUID customerId) {
    log.info("Fetching Balance for Customer: {}", customerId);
    return ledgerRepository.getCustomerAccount(customerId)
        .map(CustomerAccount::getBalance)
        .orElseThrow(() -> Problem.valueOf(Status.BAD_REQUEST, CUSTOMER_ACCOUNT_NOT_FOUND));
  }

  public synchronized TransactionDTO storeCustomerTransaction(final UUID customerId, final CreateTransactionDTO transaction) {
    final UUID transactionId = UUID.randomUUID();
    ledgerRepository.getCustomerAccount(customerId)
        .ifPresentOrElse(account -> {
          switch (transaction.getType()) {
            case DEPOSIT -> {
              final BigDecimal newBalance = account.getBalance().add(transaction.getAmount());
              account.setBalance(newBalance);
              account.getTransactions().add(Transaction.builder()
                  .id(transactionId)
                  .description(transaction.getDescription())
                  .amount(transaction.getAmount())
                  .type(transaction.getType())
                  .build());
            }
            case WITHDRAWAL -> {
              final BigDecimal newBalance = account.getBalance().subtract(transaction.getAmount());
              if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw Problem.valueOf(Status.BAD_REQUEST, INSUFFICIENT_FUNDS);
              }
              account.setBalance(newBalance);
              account.getTransactions().add(Transaction.builder()
                  .id(transactionId)
                  .description(transaction.getDescription())
                  .amount(transaction.getAmount())
                  .build());
            }
            default -> throw Problem.valueOf(Status.BAD_REQUEST, INVALID_TRANSACTION_TYPE);
          }
    }, () -> {
      throw Problem.valueOf(Status.BAD_REQUEST, CUSTOMER_ACCOUNT_NOT_FOUND);
    });
    log.info("Created Customer Transaction: {}", transactionId);
    return TransactionDTO.builder().id(transactionId).build();
  }

  public List<TransactionDTO> getCustomerTransactions(final UUID customerId) {
    log.info("Fetching Transactions for Customer: {}", customerId);
    return ledgerRepository.getCustomerAccount(customerId)
        .map(CustomerAccount::getTransactions)
        .orElseThrow(() -> Problem.valueOf(Status.BAD_REQUEST, CUSTOMER_ACCOUNT_NOT_FOUND))
        .stream()
        .map(transaction -> TransactionDTO.builder()
            .id(transaction.getId())
            .description(transaction.getDescription())
            .amount(transaction.getAmount())
            .type(transaction.getType())
            .build())
        .toList();
  }

}

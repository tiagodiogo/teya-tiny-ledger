package com.tiagodiogo.tiny_ledger.controller;

import com.tiagodiogo.tiny_ledger.domain.dto.CreateTransactionDTO;
import com.tiagodiogo.tiny_ledger.domain.dto.CustomerDTO;
import com.tiagodiogo.tiny_ledger.domain.dto.TransactionDTO;
import com.tiagodiogo.tiny_ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-ledger")
public class LedgerController {

  private final LedgerService ledgerService;

  @PostMapping("/customers")
  public ResponseEntity<CustomerDTO> postCustomer() {
    final CustomerDTO customer = ledgerService.createCustomerAccount();
    return ResponseEntity.status(HttpStatus.CREATED).body(customer);
  }

  @PostMapping("/customers/{customerId}/transactions")
  public ResponseEntity<TransactionDTO> postTransaction(@PathVariable final UUID customerId, @RequestBody final CreateTransactionDTO transaction) {
    final TransactionDTO createdTransaction = ledgerService.storeCustomerTransaction(customerId, transaction);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
  }

  @GetMapping("/customers/{customerId}/transactions")
  public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable final UUID customerId) {
    return ResponseEntity.ok(ledgerService.getCustomerTransactions(customerId));
  }

  @GetMapping("/customers/{customerId}/balance")
  public ResponseEntity<BigDecimal> getBalance(@PathVariable final UUID customerId) {
    return ResponseEntity.ok(ledgerService.getCustomerBalance(customerId));
  }


}

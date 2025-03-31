package com.tiagodiogo.tiny_ledger.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomerAccount {

  private UUID id;
  private BigDecimal balance;
  private List<Transaction> transactions;

}

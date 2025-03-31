package com.tiagodiogo.tiny_ledger.domain.entity;

import com.tiagodiogo.tiny_ledger.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class Transaction {

  private UUID id;
  private String description;
  private BigDecimal amount;
  private TransactionType type;

}

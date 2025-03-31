package com.tiagodiogo.tiny_ledger.domain.dto;

import com.tiagodiogo.tiny_ledger.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

  UUID id;
  BigDecimal amount;
  TransactionType type;
  String description;

}




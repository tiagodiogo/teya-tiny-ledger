package com.tiagodiogo.tiny_ledger.domain.dto;

import com.tiagodiogo.tiny_ledger.domain.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {

  @Min(0)
  BigDecimal amount;

  @NotNull
  TransactionType type;

  @NotBlank
  String description;

}


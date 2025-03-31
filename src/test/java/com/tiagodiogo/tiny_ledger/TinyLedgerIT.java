package com.tiagodiogo.tiny_ledger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagodiogo.tiny_ledger.domain.dto.CustomerDTO;
import com.tiagodiogo.tiny_ledger.domain.dto.TransactionDTO;
import com.tiagodiogo.tiny_ledger.domain.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TinyLedgerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void ledgerIntegrationTest() throws Exception {

    final BigDecimal initialBalance = BigDecimal.ZERO;
    final BigDecimal firstDeposit = BigDecimal.TEN;

    //Create customer
    MvcResult customerResult = mockMvc.perform(MockMvcRequestBuilders.post("/tiny-ledger/customers")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();
    CustomerDTO customer = new ObjectMapper().readValue(customerResult.getResponse().getContentAsString(), CustomerDTO.class);

    //Check Balance
    mockMvc.perform(MockMvcRequestBuilders.get("/tiny-ledger/customers/{customerId}/balance", customer.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(initialBalance.toString()));

    //Make a deposit
    TransactionDTO depositTransaction = TransactionDTO.builder().type(TransactionType.DEPOSIT).amount(firstDeposit).description("account-opening").build();
    mockMvc.perform(MockMvcRequestBuilders.post("/tiny-ledger/customers/{customerId}/transactions", customer.getId())
            .content(new ObjectMapper().writeValueAsString(depositTransaction))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    //Check Balance
    mockMvc.perform(MockMvcRequestBuilders.get("/tiny-ledger/customers/{customerId}/balance", customer.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(firstDeposit.toString()));

    //Attempt to withdraw more than the current balance
    TransactionDTO greedyTransaction = TransactionDTO.builder().type(TransactionType.WITHDRAWAL).amount(BigDecimal.valueOf(20)).description("greedy-transaction").build();
    mockMvc.perform(MockMvcRequestBuilders.post("/tiny-ledger/customers/{customerId}/transactions", customer.getId())
            .content(new ObjectMapper().writeValueAsString(greedyTransaction))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    //Withdraw all the funds
    TransactionDTO fairTransaction = TransactionDTO.builder().type(TransactionType.WITHDRAWAL).amount(firstDeposit).description("fair-transaction").build();
    mockMvc.perform(MockMvcRequestBuilders.post("/tiny-ledger/customers/{customerId}/transactions", customer.getId())
            .content(new ObjectMapper().writeValueAsString(fairTransaction))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    //Check Balance
    mockMvc.perform(MockMvcRequestBuilders.get("/tiny-ledger/customers/{customerId}/balance", customer.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(initialBalance.toString()));

    //Consult transaction list
    MvcResult transactionsResult = mockMvc.perform(MockMvcRequestBuilders.get("/tiny-ledger/customers/{customerId}/transactions", customer.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    List<TransactionDTO> transactions = new ObjectMapper().readValue(transactionsResult.getResponse().getContentAsString(), new TypeReference<>() {});
    assertEquals(2, transactions.size(), "Transaction list should contain 2 transactions");
  }

}
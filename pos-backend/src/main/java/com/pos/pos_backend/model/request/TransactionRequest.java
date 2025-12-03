package com.pos.pos_backend.model.request;

import lombok.Data;

import java.util.List;

@Data
public class TransactionRequest {
  private List<TransactionItemRequest> items;
}

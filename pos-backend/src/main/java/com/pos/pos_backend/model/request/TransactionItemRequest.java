package com.pos.pos_backend.model.request;

import lombok.Data;

@Data
public class TransactionItemRequest {
  private Long productId;
  private Integer quantity;
}

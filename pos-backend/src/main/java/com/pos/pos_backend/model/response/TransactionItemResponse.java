package com.pos.pos_backend.model.response;

import lombok.Data;

@Data
public class TransactionItemResponse {
  private Long productId;
  private String productName;
  private Integer quantity;
  private Long price;
}

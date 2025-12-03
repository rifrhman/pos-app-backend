package com.pos.pos_backend.model.request;

import lombok.Data;

@Data
public class SyncTransactionItemUpload {
  private Long productId;
  private Integer quantity;
  private Long price;
}

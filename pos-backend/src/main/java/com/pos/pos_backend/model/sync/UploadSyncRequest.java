package com.pos.pos_backend.model.sync;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UploadSyncRequest {

  @Data
  public static class TransactionUpload{
    private String localId;
    private Long total;
    private LocalDateTime createdAt;
    private List<TransactionItemUpload> items;
  }

  @Data
  public static class TransactionItemUpload{
    private Long productId;
    private Integer quantity;
    private Long price;
  }

  private List<TransactionUpload> transactions;

}

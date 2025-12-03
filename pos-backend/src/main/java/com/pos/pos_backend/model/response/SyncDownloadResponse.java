package com.pos.pos_backend.model.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SyncDownloadResponse {

  private LocalDateTime serverTime;
  private List<ProductResponse> products;
  private List<TransactionResponse> transactions;

}

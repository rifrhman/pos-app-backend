package com.pos.pos_backend.model.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SyncTransactionUpload {
  private String localId; // id transaction from device
  private Long total;
  private LocalDateTime createdAt;
  private List<SyncTransactionItemUpload> items;
}

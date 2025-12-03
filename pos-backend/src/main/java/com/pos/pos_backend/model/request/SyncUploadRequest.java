package com.pos.pos_backend.model.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SyncUploadRequest {
  private List<SyncTransactionUpload> transactions;
}


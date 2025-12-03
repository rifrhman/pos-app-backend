package com.pos.pos_backend.model.sync;

import lombok.Data;

import java.util.Map;

@Data
public class UploadSyncResponse {

  private boolean success;
  private Map<String, Long> serverIds;

}

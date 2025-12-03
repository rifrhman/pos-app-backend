package com.pos.pos_backend.service.services;

import com.pos.pos_backend.model.request.SyncUploadRequest;
import com.pos.pos_backend.model.response.SyncDownloadResponse;

public interface SyncService {

  void upload(SyncUploadRequest syncUploadRequest);

  SyncDownloadResponse download(String lastSync);

}

package com.pos.pos_backend.service.sync11;

import com.pos.pos_backend.model.sync.ChangesSyncResponse;
import com.pos.pos_backend.model.sync.InitSyncResponse;
import com.pos.pos_backend.model.sync.UploadSyncRequest;
import com.pos.pos_backend.model.sync.UploadSyncResponse;

import java.time.LocalDateTime;

public interface SyncService11 {

  InitSyncResponse initSync();

  UploadSyncResponse uploadSync(UploadSyncRequest uploadSyncRequest);

  ChangesSyncResponse getChangesSince(LocalDateTime timestamp);

}

package com.pos.pos_backend.controller;

import com.pos.pos_backend.model.request.SyncUploadRequest;
import com.pos.pos_backend.model.response.SyncDownloadResponse;
import com.pos.pos_backend.service.services.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync")
public class SyncController {

  private final SyncService syncService;

  @PostMapping("/upload")
  public ResponseEntity<?> upload(@RequestBody SyncUploadRequest syncUploadRequest){
    syncService.upload(syncUploadRequest);
    return ResponseEntity.ok("OK");
  }

  @GetMapping("/download")
  public ResponseEntity<SyncDownloadResponse> download(@RequestParam(required = false) String lastSync){
    return new ResponseEntity<>(syncService.download(lastSync), HttpStatus.OK);
  }

}

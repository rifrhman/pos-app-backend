package com.pos.pos_backend.model.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionResponse {

  private Long id;
  private Long total;
  private LocalDateTime createdAt;
  private List<TransactionItemResponse> items;

}

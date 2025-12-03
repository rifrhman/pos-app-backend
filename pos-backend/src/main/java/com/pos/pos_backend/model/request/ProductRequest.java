package com.pos.pos_backend.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductRequest {
  private String name;
  private Long price;
  private Integer stock;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

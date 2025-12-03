package com.pos.pos_backend.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {
  private Long id;
  private String name;
  private Long price;
  private Integer stock;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

package com.pos.pos_backend.model.sync;

import com.pos.pos_backend.entity.Product;
import com.pos.pos_backend.entity.Transaction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChangesSyncResponse {

  private List<Product> products;
  private List<Transaction> transactions;
  private LocalDateTime timestamp;

}

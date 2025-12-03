package com.pos.pos_backend.repository;

import com.pos.pos_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.items i LEFT JOIN FETCH i.product")
  List<Transaction> findAllWithItems();

  @Query("select t from Transaction t where t.createdAt > :lastSync")
  List<Transaction> findAllAfterCreatedAt(LocalDateTime lastSync);

}

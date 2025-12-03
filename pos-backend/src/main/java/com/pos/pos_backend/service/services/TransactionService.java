package com.pos.pos_backend.service.services;

import com.pos.pos_backend.model.request.TransactionRequest;
import com.pos.pos_backend.model.response.TransactionItemResponse;
import com.pos.pos_backend.model.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

  TransactionResponse create(TransactionRequest transactionRequest);
//  List<TransactionResponse> getAll();
  TransactionResponse getById(Long id);

  List<TransactionResponse> getAllTransactions();

}

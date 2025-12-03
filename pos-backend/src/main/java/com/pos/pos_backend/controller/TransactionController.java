package com.pos.pos_backend.controller;

import com.pos.pos_backend.model.request.TransactionRequest;
import com.pos.pos_backend.model.response.TransactionResponse;
import com.pos.pos_backend.service.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping
  public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest transactionRequest){
    return new ResponseEntity<>(transactionService.create(transactionRequest), HttpStatus.CREATED);
  }

//  @GetMapping
//  public ResponseEntity<List<TransactionResponse>> getAll(){
//    return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
//  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionResponse> getById(@PathVariable Long id){
    return new ResponseEntity<>(transactionService.getById(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<TransactionResponse>> getAllTransactions(){
    return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
  }

}

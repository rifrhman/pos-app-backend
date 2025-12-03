package com.pos.pos_backend.service.impl;

import com.pos.pos_backend.entity.Product;
import com.pos.pos_backend.entity.Transaction;
import com.pos.pos_backend.entity.TransactionItem;
import com.pos.pos_backend.model.request.TransactionRequest;
import com.pos.pos_backend.model.response.TransactionItemResponse;
import com.pos.pos_backend.model.response.TransactionResponse;
import com.pos.pos_backend.repository.ProductRepository;
import com.pos.pos_backend.repository.TransactionItemRepository;
import com.pos.pos_backend.repository.TransactionRepository;
import com.pos.pos_backend.service.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionItemRepository transactionItemRepository;
  private final ProductRepository productRepository;


  @Override
  @Transactional
  public TransactionResponse create(TransactionRequest transactionRequest) {

    // New Transaction object
    Transaction transaction = new Transaction();

    AtomicLong total = new AtomicLong(0L);

    List<TransactionItem> items = transactionRequest.getItems().stream().map(
            reqItem -> {
              Product product = productRepository.findById(reqItem.getProductId())
                      .orElseThrow(() -> new RuntimeException("Product not found: " + reqItem.getProductId()));
              if(product.getStock() < reqItem.getQuantity()){
                throw new RuntimeException("Not enough stock for: " + product.getName());
              }

              // update stock
              product.setStock(product.getStock() - reqItem.getQuantity());
              productRepository.save(product);

              // price * qty
              long itemPrice = product.getPrice() * reqItem.getQuantity();
              total.addAndGet(itemPrice);

              TransactionItem transactionItem = new TransactionItem();
              transactionItem.setProduct(product);
              transactionItem.setQuantity(reqItem.getQuantity());
              transactionItem.setPrice(itemPrice);
              transactionItem.setTransaction(transaction);
              return transactionItem;
            }
    ).collect(Collectors.toList());

    transaction.setTotal(total.get());
    transaction.setItems(items);

    Transaction saved = transactionRepository.save(transaction);

    // save items
    transactionItemRepository.saveAll(items);

    return mappingTransactionToTransactionResponse(saved);
  }

//  @Override
//  public List<TransactionResponse> getAll() {
//    return transactionRepository.findAll().stream()
//            .map(this::mappingTransactionToTransactionResponse)
//            .toList();
//  }

  @Override
  public TransactionResponse getById(Long id) {
    Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction is not found"));
    return mappingTransactionToTransactionResponse(transaction);
  }

  @Override
  public List<TransactionResponse> getAllTransactions() {
    List<Transaction> transactions = transactionRepository.findAllWithItems();

    return transactions.stream().map(transaction ->
            {
              TransactionResponse transactionResponse = new TransactionResponse();
              transactionResponse.setId(transaction.getId());
              transactionResponse.setTotal(transaction.getTotal());
              transactionResponse.setCreatedAt(transaction.getCreatedAt());

              List<TransactionItemResponse> itemResponses = transaction.getItems().stream()
                      .map(i -> {
                        TransactionItemResponse itemRes = new TransactionItemResponse();
                        itemRes.setProductId(i.getProduct().getId());
                        itemRes.setProductName(i.getProduct().getName());
                        itemRes.setPrice(i.getPrice());
                        itemRes.setQuantity(i.getQuantity());
                        return itemRes;
                      }).toList();
              transactionResponse.setItems(itemResponses);
              return transactionResponse;
            }).collect(Collectors.toList());
  }

  private TransactionItemResponse mappingTransactionToTransactionItemResponse(TransactionItem transactionItem){
    TransactionItemResponse transactionItemResponse = new TransactionItemResponse();
    transactionItemResponse.setProductId(transactionItem.getProduct().getId());
    transactionItemResponse.setProductName(transactionItem.getProduct().getName());
    transactionItemResponse.setQuantity(transactionItem.getQuantity());
    transactionItemResponse.setPrice(transactionItem.getPrice());
    return transactionItemResponse;
  }

  private TransactionResponse mappingTransactionToTransactionResponse(Transaction transaction){
    TransactionResponse transactionResponse = new TransactionResponse();
    transactionResponse.setId(transaction.getId());
    transactionResponse.setTotal(transaction.getTotal());
    transactionResponse.setCreatedAt(transaction.getCreatedAt());
    transactionResponse.setItems(
            transaction.getItems().stream()
                    .map(this::mappingTransactionToTransactionItemResponse)
                    .collect(Collectors.toList())
    );
    return transactionResponse;
  }

}

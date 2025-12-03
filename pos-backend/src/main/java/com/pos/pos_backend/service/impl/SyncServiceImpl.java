package com.pos.pos_backend.service.impl;

import com.pos.pos_backend.entity.Product;
import com.pos.pos_backend.entity.Transaction;
import com.pos.pos_backend.entity.TransactionItem;
import com.pos.pos_backend.model.request.SyncTransactionItemUpload;
import com.pos.pos_backend.model.request.SyncTransactionUpload;
import com.pos.pos_backend.model.request.SyncUploadRequest;
import com.pos.pos_backend.model.response.ProductResponse;
import com.pos.pos_backend.model.response.SyncDownloadResponse;
import com.pos.pos_backend.model.response.TransactionItemResponse;
import com.pos.pos_backend.model.response.TransactionResponse;
import com.pos.pos_backend.repository.ProductRepository;
import com.pos.pos_backend.repository.TransactionItemRepository;
import com.pos.pos_backend.repository.TransactionRepository;
import com.pos.pos_backend.service.services.SyncService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {

  private final ProductRepository productRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionItemRepository transactionItemRepository;

  @Override
  @Transactional
  public void upload(SyncUploadRequest syncUploadRequest) {

    for (SyncTransactionUpload t : syncUploadRequest.getTransactions()) {

      Transaction newTransaction = new Transaction();
      newTransaction.setTotal(t.getTotal());
      newTransaction.setCreatedAt(t.getCreatedAt());

      Transaction savedTr = transactionRepository.save(newTransaction);

      for (SyncTransactionItemUpload item : t.getItems()) {

        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update stock
        product.setStock(product.getStock() - item.getQuantity());
        productRepository.save(product);

        TransactionItem transactionItem = new TransactionItem();
        transactionItem.setTransaction(savedTr);
        transactionItem.setProduct(product);
        transactionItem.setQuantity(item.getQuantity());
        transactionItem.setPrice(item.getPrice());

        transactionItemRepository.save(transactionItem);
      }
    }
  }

  @Override
  public SyncDownloadResponse download(String lastSync) {

    LocalDateTime last = lastSync == null ? LocalDateTime.MIN : LocalDateTime.parse(lastSync);

    SyncDownloadResponse response = new SyncDownloadResponse();
    response.setServerTime(LocalDateTime.now());

    // get product updated
    List<Product> updatedProducts = productRepository.findAll();

    List<ProductResponse> productResponses = updatedProducts.stream().map(
            product -> {
              ProductResponse productResponse = new ProductResponse();
              productResponse.setId(product.getId());
              productResponse.setName(product.getName());
              productResponse.setPrice(product.getPrice());
              productResponse.setStock(product.getStock());
              productResponse.setCreatedAt(product.getCreatedAt());
              productResponse.setUpdatedAt(product.getUpdatedAt());
              return productResponse;
            }).toList();

    // get newest transaction
    List<Transaction> transactions = transactionRepository.findAllAfterCreatedAt(last);

    List<TransactionResponse> transactionResponses = transactions.stream().map(
            transaction -> {
              TransactionResponse res = new TransactionResponse();
              res.setId(transaction.getId());
              res.setTotal(transaction.getTotal());
              res.setCreatedAt(transaction.getCreatedAt());

              res.setItems(
                      transaction.getItems().stream().map(i -> {
                        TransactionItemResponse item = new TransactionItemResponse();
                        item.setProductId(i.getProduct().getId());
                        item.setProductName(i.getProduct().getName());
                        item.setQuantity(i.getQuantity());
                        item.setPrice(i.getPrice());
                        return item;
                      }).collect(Collectors.toList()));
              return res;
            }).toList();

    response.setProducts(productResponses);
    response.setTransactions(transactionResponses);

    return response;
  }
}

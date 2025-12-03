package com.pos.pos_backend.service.sync11;

import com.pos.pos_backend.entity.Product;
import com.pos.pos_backend.entity.Transaction;
import com.pos.pos_backend.entity.TransactionItem;
import com.pos.pos_backend.model.sync.ChangesSyncResponse;
import com.pos.pos_backend.model.sync.InitSyncResponse;
import com.pos.pos_backend.model.sync.UploadSyncRequest;
import com.pos.pos_backend.model.sync.UploadSyncResponse;
import com.pos.pos_backend.repository.ProductRepository;
import com.pos.pos_backend.repository.TransactionItemRepository;
import com.pos.pos_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SyncServiceImpl11 implements SyncService11 {

  private final ProductRepository productRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionItemRepository transactionItemRepository;

  // ---------------------- INIT SYNC ----------------------

  @Override
  public InitSyncResponse initSync() {
    InitSyncResponse initSyncResponse = new InitSyncResponse();

    initSyncResponse.setProducts(productRepository.findAll());
    initSyncResponse.setTransactions(transactionRepository.findAll());

    initSyncResponse.setTimestamp(LocalDateTime.now());

    return initSyncResponse;
  }

  // ---------------------- UPLOAD OFFLINE DATA ----------------------

  @Override
  public UploadSyncResponse uploadSync(UploadSyncRequest uploadSyncRequest) {
    Map<String, Long> idMapping = new HashMap<>();

    for (UploadSyncRequest.TransactionUpload transactionUpload : uploadSyncRequest.getTransactions()){

      // 1.Create new transactions
      Transaction transaction = new Transaction();
      transaction.setTotal(transactionUpload.getTotal());
      transaction.setCreatedAt(transactionUpload.getCreatedAt());
      transaction.setUpdatedAt(LocalDateTime.now());
      transaction = transactionRepository.save(transaction);

      // save mapping localId -> server
      idMapping.put(transactionUpload.getLocalId(), transaction.getId());

      // 2. Save item transactions
      List<TransactionItem> items = new ArrayList<>();

      for (UploadSyncRequest.TransactionItemUpload transactionItemUpload : transactionUpload.getItems()){
        Optional<Product> productOpt = productRepository.findById(transactionItemUpload.getProductId());

        if(productOpt.isEmpty()) continue;

        Product product = productOpt.get();

        // decrease the stock
        product.setStock(product.getStock() - transactionItemUpload.getQuantity());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        TransactionItem transactionItem = new TransactionItem();
        transactionItem.setTransaction(transaction);
        transactionItem.setProduct(product);
        transactionItem.setQuantity(transactionItemUpload.getQuantity());
        transactionItem.setPrice(transactionItemUpload.getPrice());

        items.add(transactionItem);
      }
      transactionItemRepository.saveAll(items);
    }
    UploadSyncResponse uploadSyncResponse = new UploadSyncResponse();
    uploadSyncResponse.setSuccess(true);
    uploadSyncResponse.setServerIds(idMapping);
    return uploadSyncResponse;
  }

  // ---------------------- GET CHANGES ----------------------

  @Override
  public ChangesSyncResponse getChangesSince(LocalDateTime timestamp) {

    ChangesSyncResponse changesSyncResponse = new ChangesSyncResponse();

    List<Product> changedProducts = productRepository.findAll().stream()
            .filter(product -> product.getUpdatedAt() != null && product.getUpdatedAt().isAfter(timestamp))
            .toList();

    List<Transaction> changedTransactions = transactionRepository.findAll().stream()
            .filter(transaction -> transaction.getUpdatedAt() != null && transaction.getUpdatedAt().isAfter(timestamp))
            .toList();

    changesSyncResponse.setProducts(changedProducts);
    changesSyncResponse.setTransactions(changedTransactions);
    changesSyncResponse.setTimestamp(LocalDateTime.now());

    return changesSyncResponse;
  }
}

package com.pos.pos_backend.service.services;

import com.pos.pos_backend.model.request.ProductRequest;
import com.pos.pos_backend.model.response.ProductResponse;

import java.util.List;

public interface ProductService {

  ProductResponse create(ProductRequest productRequest);
  List<ProductResponse> getAll();
  ProductResponse update(Long id, ProductRequest productRequest);
  ProductResponse getById(Long id);
  void delete(Long id);

}

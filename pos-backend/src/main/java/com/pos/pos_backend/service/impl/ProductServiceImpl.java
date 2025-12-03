package com.pos.pos_backend.service.impl;

import com.pos.pos_backend.entity.Product;
import com.pos.pos_backend.model.request.ProductRequest;
import com.pos.pos_backend.model.response.ProductResponse;
import com.pos.pos_backend.repository.ProductRepository;
import com.pos.pos_backend.service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public ProductResponse create(ProductRequest productRequest) {
    Product product = new Product();
    product.setName(productRequest.getName());
    product.setPrice(productRequest.getPrice());
    product.setStock(productRequest.getStock());
    product.setCreatedAt(productRequest.getCreatedAt());
    product.setUpdatedAt(productRequest.getUpdatedAt());
    return mappingProductToProductResponse(productRepository.save(product));
  }

  @Override
  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream().map(
            this::mappingProductToProductResponse
    ).collect(Collectors.toList());
  }

  @Override
  public ProductResponse update(Long id, ProductRequest productRequest) {
    Product product = productRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Product is not found")
    );
    product.setName(productRequest.getName());
    product.setStock(productRequest.getStock());
    product.setPrice(productRequest.getPrice());
    return mappingProductToProductResponse(productRepository.save(product));
  }

  @Override
  public ProductResponse getById(Long id) {
    Product product = productRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Product not found")
    );
    return mappingProductToProductResponse(product);
  }

  @Override
  public void delete(Long id) {
    productRepository.deleteById(id);
  }

  private ProductResponse mappingProductToProductResponse(Product product){
    ProductResponse productResponse = new ProductResponse();
    productResponse.setId(product.getId());
    productResponse.setName(product.getName());
    productResponse.setStock(product.getStock());
    productResponse.setPrice(product.getPrice());
    productResponse.setCreatedAt(product.getCreatedAt());
    productResponse.setUpdatedAt(product.getUpdatedAt());
    return productResponse;
  }

}

package com.pos.pos_backend.controller;

import com.pos.pos_backend.model.request.ProductRequest;
import com.pos.pos_backend.model.response.ProductResponse;
import com.pos.pos_backend.service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;


  @PostMapping
  public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest){
    return new ResponseEntity<>(productService.create(productRequest), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAll(){
    return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getById(@PathVariable Long id){
    return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest productRequest){
    return new ResponseEntity<>(productService.update(id, productRequest), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }


}

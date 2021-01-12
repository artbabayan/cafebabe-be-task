package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.Product;

public interface ProductService {

    Product createProduct(Product product);

    Product findById(Long productId);

    void updateName(String name, Long productId);

}

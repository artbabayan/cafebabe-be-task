package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.Product;
import com.babayan.babe.cafe.app.model.dto.ProductInOrder;
import com.babayan.babe.cafe.app.model.enums.ProductInOrderStatusEnum;

import java.util.Set;

public interface ProductInOrderService {

    ProductInOrder createProductInOrder(ProductInOrder productInOrder, Long orderId, Set<Product> products);

    ProductInOrder findById(Long id);

    ProductInOrder updateStatus(Long id, ProductInOrderStatusEnum status);

}

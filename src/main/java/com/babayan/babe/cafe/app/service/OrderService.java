package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.Order;
import com.babayan.babe.cafe.app.model.enums.OrderStatusEnum;

public interface OrderService {

    Order createOrder(Order order, int tableNumber, Long productInOrderId, Long userId);

    Order findById(Long orderId);

    Order updateOrderStatus(Long orderId, OrderStatusEnum status);
}

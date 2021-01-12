package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.exceptions.InputMismatchException;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.OperationFailedException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.model.dao.OrderEntity;
import com.babayan.babe.cafe.app.model.dto.Order;
import com.babayan.babe.cafe.app.model.dto.ProductInOrder;
import com.babayan.babe.cafe.app.model.dto.Table;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.OrderStatusEnum;
import com.babayan.babe.cafe.app.model.enums.TableStatusEnum;
import com.babayan.babe.cafe.app.repository.OrderRepository;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.OrderService;
import com.babayan.babe.cafe.app.service.ProductInOrderService;
import com.babayan.babe.cafe.app.service.TableService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    @Autowired public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private TableService tableService;
    @Autowired public void setTableService(TableService tableService) {
        this.tableService = tableService;
    }

    private ProductInOrderService productInOrderService;
    @Autowired public void setProductInOrderService(ProductInOrderService productInOrderService) {
        this.productInOrderService = productInOrderService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <ORDER SERVICE>

    @PostConstruct
    private void init() {
        log.info("Order service initialized");
    }

    /**
     * @see OrderService#createOrder(Order, int, Long, Long)
     */
    @Transactional
    @Override
    public Order createOrder(Order order, int tableNumber, Long productInOrderId, Long userId) {
        if (order == null || productInOrderId == null || userId == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }
        if (order.getId() != null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.non.null.id"));
        }

        Table table = tableService.findByTableNumber(tableNumber);
        if (table.getStatus() == TableStatusEnum.AVAILABLE || table.getStatus() == TableStatusEnum.DIRTY) {
            throw new OperationFailedException("Wrong table status");
        }

        User owner = table.getOwner();
        if (owner == null) {
            throw new OperationFailedException("Table doesn't has exist user.");
        }

        if (!owner.getId().equals(userId)) {
            log.error(String.format("Input user id [%s] and table owner id [%s] is mismatch", userId, owner.getId()));
            throw new InputMismatchException("Different inputs");
        }

        ProductInOrder productInOrder = productInOrderService.findById(productInOrderId);

        order.setProductInOrder(productInOrder);
        order.setOwner(owner); //set order owner
        order.setTable(table); //set order table
        order.setStatus(OrderStatusEnum.AWAITING); //set order status

        OrderEntity entity = mapper.map(order, OrderEntity.class);
        // save/store user entity
        entity = orderRepository.saveAndFlush(entity);
        log.info(String.format("New order successfully created, id [%s]", entity.getId()));

        return mapper.map(entity, Order.class);
    }

    /**
     * @see OrderService#findById(Long)
     */
    @Override
    public Order findById(Long orderId) {
        if (orderId == null) {
            throw new ValidationException("Unable to process for null object.");
        }

        Optional<OrderEntity> optional = orderRepository.findById(orderId);
        if (!optional.isPresent()){
            log.error(String.format("Order with orderId [%s] not found.", orderId));
            throw new NotFoundException(messageService.getMessage("error.order.not.found"));
        }
        log.info(String.format("Order with orderId [%s] successfully fetched", orderId));
        return mapper.map(optional.get(), Order.class);
    }

    /**
     * @see OrderService#updateOrderStatus(Long, OrderStatusEnum)
     */
    @Transactional
    @Override
    public Order updateOrderStatus(Long orderId, OrderStatusEnum status) {
        Order order = findById(orderId);

        orderRepository.updateOrderStatus(orderId, status);
        log.info(String.format("Order with orderId [%s] is successfully updated", order.getId()));

        return findById(order.getId());
    }

    //endregion

}

package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.model.dao.ProductInOrderEntity;
import com.babayan.babe.cafe.app.model.dto.Order;
import com.babayan.babe.cafe.app.model.dto.Product;
import com.babayan.babe.cafe.app.model.dto.ProductInOrder;
import com.babayan.babe.cafe.app.model.enums.ProductInOrderStatusEnum;
import com.babayan.babe.cafe.app.repository.ProductInOrderRepository;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.OrderService;
import com.babayan.babe.cafe.app.service.ProductInOrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class ProductInOrderServiceImpl implements ProductInOrderService {
    private ProductInOrderRepository repository;
    @Autowired public void setRepository(ProductInOrderRepository repository) {
        this.repository = repository;
    }

    private OrderService orderService;
    @Autowired public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <PRODUCT IN ORDER SERVICE>

    @PostConstruct
    private void init() {
        log.info("ProductInOrderService service initialized");
    }

    /**
     * @see ProductInOrderService#createProductInOrder(ProductInOrder, Long, Set)
     */
    @Override
    public ProductInOrder createProductInOrder(ProductInOrder productInOrder, Long orderId, Set<Product> products) {
        if (productInOrder == null || orderId == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }
        if (productInOrder.getId() != null) {
            throw new ValidationException("Unable to process for non null productInOrder id.");
        }

        if (CollectionUtils.isEmpty(products)) {
            throw new ValidationException("Unable to process for null products.");
        }

        Order order = orderService.findById(orderId);
        productInOrder.setOrder(order);
        productInOrder.setStatusEnum(ProductInOrderStatusEnum.ACTIVE);
        productInOrder.setProducts(products);

        List<BigDecimal> list = new ArrayList<>();
        for (Product product : products) {
            BigDecimal price = product.getPrice();
            int countOfProduct = product.getCountOfProduct();

            BigDecimal multiply = price.multiply(BigDecimal.valueOf(countOfProduct));
            list.add(multiply);
        }

        BigDecimal total = list.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        productInOrder.setTotalPrice(total);

        ProductInOrderEntity entity = mapper.map(productInOrder, ProductInOrderEntity.class);
        // save/store user entity
        entity = repository.saveAndFlush(entity);
        log.info(String.format("New productInOrderEntity successfully created, id [%s]", entity.getId()));

        return mapper.map(entity, ProductInOrder.class);
    }

    /**
     * @see ProductInOrderService#findById(Long)
     */
    @Override
    public ProductInOrder findById(Long id) {
        if (id == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        Optional<ProductInOrderEntity> optional = repository.findById(id);
        if (!optional.isPresent()){
            log.error(String.format("ProductInOrder with id [%s] not found.", id));
            throw new NotFoundException(messageService.getMessage("error.order.not.found"));
        }
        log.info(String.format("ProductInOrder with id [%s] successfully fetched", id));
        return mapper.map(optional.get(), ProductInOrder.class);
    }

    /**
     * @see ProductInOrderService#updateStatus(Long, ProductInOrderStatusEnum)
     */
    @Override
    public ProductInOrder updateStatus(Long id, ProductInOrderStatusEnum status) {
        ProductInOrder productInOrder = findById(id);

        repository.updateStatus(id, status);
        log.info(String.format("Order with orderId [%s] is successfully updated", productInOrder.getId()));

        return findById(productInOrder.getId());
    }

    //endregion

}

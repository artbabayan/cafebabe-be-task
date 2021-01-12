package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.model.dto.Order;
import com.babayan.babe.cafe.app.model.enums.OrderStatusEnum;
import com.babayan.babe.cafe.app.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author artbabayan
 */
@Validated
@Log4j2
@RestController
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private OrderService orderService;
    @Autowired public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    /**
     * Initializes a new instance of the class
     */
    public OrderController() {
        log.info("Orders API initialized");
    }

    // region <ORDER CONTROLLER>

    /**
     * Create new order for table
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order,
                                             @Valid @NotNull @RequestParam(value = "tableNumber") int number,
                                             @Valid @NotNull @RequestParam(value = "productInOrderId") long productInOrderId,
                                             @Valid @NotNull @RequestParam(value = "userId") long userId) {

        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        Order newOrder = orderService.createOrder(order, number, productInOrderId, userId);
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }

    /**
     * Gets order by specific id
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") long id) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        Order order = orderService.findById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Updates order by specific id
     */
    @PutMapping()
    public ResponseEntity<Order> updateOrderStatus(@Valid @NotNull @RequestParam(value = "orderId") long orderId,
                                                   @Valid @NotNull @RequestParam(value = "status") OrderStatusEnum status) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        Order order = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //endregion

}

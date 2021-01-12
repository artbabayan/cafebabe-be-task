package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.model.dto.Order;
import com.babayan.babe.cafe.app.model.dto.Product;
import com.babayan.babe.cafe.app.model.dto.ProductInOrder;
import com.babayan.babe.cafe.app.model.enums.ProductInOrderStatusEnum;
import com.babayan.babe.cafe.app.service.ProductInOrderService;
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
import java.util.Set;

/**
 * @author artbabayan
 */
@Validated
@Log4j2
@RestController
@RequestMapping(value = "/api/v1/product-in-orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductInOrderController {
    private ProductInOrderService productInOrderService;
    @Autowired public void setProductInOrderService(ProductInOrderService productInOrderService) {
        this.productInOrderService = productInOrderService;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    /**
     * Initializes a new instance of the class
     */
    public ProductInOrderController() {
        log.info("ProductInOrder API initialized");
    }

    // region <PRODUCT IN ORDER CONTROLLER>

    /**
     * Creates new product
     */
    @PostMapping
    public ResponseEntity<ProductInOrder> createProductInOrder(@RequestBody @Valid ProductInOrder productInOrder,
                                                               @Valid @NotNull @RequestParam(value = "orderId") long orderId,
                                                               @Valid @NotNull @RequestParam(value = "products") Set<Product> products) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        ProductInOrder newProductInOrder = productInOrderService.createProductInOrder(productInOrder, orderId, products);
        return new ResponseEntity<>(newProductInOrder, HttpStatus.OK);
    }

    /**
     * Gets productInOrder by specific id
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<ProductInOrder> findById(@PathVariable("id") long id) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        ProductInOrder productInOrder = productInOrderService.findById(id);
        return new ResponseEntity<>(productInOrder, HttpStatus.OK);
    }

    /**
     * Updates order by specific id
     */
    @PutMapping()
    public ResponseEntity<Order> updateOrderStatus(@Valid @NotNull @RequestParam(value = "orderId") long orderId,
                                                   @Valid @NotNull @RequestParam(value = "status") ProductInOrderStatusEnum status) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        productInOrderService.updateStatus(orderId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //endregion

}

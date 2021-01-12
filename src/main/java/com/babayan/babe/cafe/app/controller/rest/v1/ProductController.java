package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.model.dto.Product;
import com.babayan.babe.cafe.app.service.ProductService;
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

/**
 * @author artbabayan
 */
@Validated
@Log4j2
@RestController
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    private ProductService productService;
    @Autowired public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    /**
     * Initializes a new instance of the class
     */
    public ProductController() {
        log.info("Products API initialized");
    }

    // region <PRODUCT CONTROLLER>

    /**
     * Creates new product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Product newProduct = productService.createProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }

    /**
     * Creates new product
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") long id) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Product newProduct = productService.findById(id);
        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }


    /**
     * Updates product name by specefic id
     */
    @PutMapping()
    public ResponseEntity<Product> updateName(@RequestParam(value = "name") String name,
                                              @RequestParam(value = "productId") long productId) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        productService.updateName(name, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //endregion

}

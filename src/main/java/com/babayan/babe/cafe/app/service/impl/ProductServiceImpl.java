package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.model.dao.ProductEntity;
import com.babayan.babe.cafe.app.model.dto.Product;
import com.babayan.babe.cafe.app.repository.ProductRepository;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    @Autowired public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <USER SERVICE>

    @PostConstruct
    private void init() {
        log.info("User service initialized");
    }

    /**
     * @see ProductService#createProduct(Product)}
     */
    @Transactional
    @Override
    public Product createProduct(Product product) {
        if (product == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }
        if (product.getId() != null) {
            throw new ValidationException("Unable to process for non null product id.");
        }

        ProductEntity entity = mapper.map(product, ProductEntity.class);

        // save/store user entity
        entity = productRepository.saveAndFlush(entity);
        log.info(String.format("New product successfully created, id [%s]", entity.getId()));

       return mapper.map(entity, Product.class);
    }

    /**
     * @see ProductService#findById(Long)
     */
    @Override
    public Product findById(Long productId) {
        if (productId == null) {
            throw new ValidationException("Unable to process for null object.");
        }

        Optional<ProductEntity> optional = productRepository.findById(productId);
        if (!optional.isPresent()){
            log.error(String.format("Product with id [%s] not found.", productId));
            throw new NotFoundException(messageService.getMessage("error.product.not.found"));
        }
        log.info(String.format("Product with id [%s] successfully fetched", productId));
        return mapper.map(optional.get(), Product.class);
    }

    /**
     * @see ProductService#updateName(String, Long)
     */
    @Transactional
    @Override
    public void updateName(String name, Long productId) {
        if (StringUtils.isBlank(name) || productId == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        Product product = findById(productId);
        // apply/save the updates
        productRepository.updateProductName(productId, name);
        log.info(String.format("Product with id [%s] is successfully updated name", product.getId()));
    }

    //endregion

}

package com.babayan.babe.cafe.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends AbstractModel {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String category;

    private int countOfProduct;

    private BigDecimal price;

    private ProductInOrder productInOrder;

}

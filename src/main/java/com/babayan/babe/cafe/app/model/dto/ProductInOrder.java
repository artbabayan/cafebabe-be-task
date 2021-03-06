package com.babayan.babe.cafe.app.model.dto;

import com.babayan.babe.cafe.app.model.enums.ProductInOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductInOrder extends AbstractModel {
    private Long id;

    @NotEmpty
    private ProductInOrderStatusEnum statusEnum;

    private Order order;

    private Set<Product> products;

    private BigDecimal totalPrice;
}

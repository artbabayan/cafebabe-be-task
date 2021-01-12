package com.babayan.babe.cafe.app.model.dto;

import com.babayan.babe.cafe.app.model.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Order extends AbstractModel {
    private Long id;

    private OrderStatusEnum status;

    private Table table;

    private ProductInOrder productInOrder;

    private User owner;

}

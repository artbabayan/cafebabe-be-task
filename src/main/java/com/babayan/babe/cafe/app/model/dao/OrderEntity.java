package com.babayan.babe.cafe.app.model.dao;

import com.babayan.babe.cafe.app.model.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author by artbabayan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "cb_order")
public class OrderEntity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatusEnum status;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    @OneToOne
    @JoinColumn(name = "table_id", nullable = false)
    private TableEntity tableEntity;

    @OneToOne
    @JoinColumn(name = "productInOrder_id", nullable = false)
    private ProductInOrderEntity productInOrderEntity;

}

package com.babayan.babe.cafe.app.model.dao;

import com.babayan.babe.cafe.app.model.enums.ProductInOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
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
@Entity
@Table(name = "product_in_order")
public class ProductInOrderEntity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductInOrderStatusEnum statusEnum;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @DecimalMin(value = "0.01", message = "Minimal price is 0.01$")
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "productInOrderEntity",
            targetEntity = ProductEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<ProductEntity> productEntities;
}

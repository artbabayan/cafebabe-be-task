package com.babayan.babe.cafe.app.repository;

import com.babayan.babe.cafe.app.model.dao.OrderEntity;
import com.babayan.babe.cafe.app.model.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsById(long orderId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update OrderEntity oe SET oe.status = :status where oe.id = :id")
    int updateOrderStatus(@Param("id") long id, @Param("status") OrderStatusEnum status);

}

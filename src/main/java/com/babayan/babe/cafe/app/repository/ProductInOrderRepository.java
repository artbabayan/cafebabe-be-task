package com.babayan.babe.cafe.app.repository;

import com.babayan.babe.cafe.app.model.dao.ProductInOrderEntity;
import com.babayan.babe.cafe.app.model.enums.ProductInOrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductInOrderRepository extends JpaRepository<ProductInOrderEntity, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ProductInOrderEntity pio SET pio.statusEnum = :status where pio.id = :id")
    int updateStatus(@Param("id") long id, @Param("status") ProductInOrderStatusEnum status);

}

package com.babayan.babe.cafe.app.repository;

import com.babayan.babe.cafe.app.model.dao.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ProductEntity pe SET pe.name = :name where pe.id = :id")
    int updateProductName(@Param("id") long id, @Param("name") String name);

}

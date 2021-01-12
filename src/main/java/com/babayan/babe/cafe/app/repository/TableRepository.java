package com.babayan.babe.cafe.app.repository;

import com.babayan.babe.cafe.app.model.dao.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    TableEntity findById(long id);

    TableEntity findByTableNumber(int number);

    TableEntity findByOwnerId(long ownerId);

    TableEntity findByOwnerEmail(String email);

    boolean existsByTableNumber(int number);

}

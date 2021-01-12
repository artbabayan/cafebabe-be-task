package com.babayan.babe.cafe.app.repository;

import com.babayan.babe.cafe.app.model.dao.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findById(long id);

    PermissionEntity findByName(String name);

    boolean existsByName(String name);

}

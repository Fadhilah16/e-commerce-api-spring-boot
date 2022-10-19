package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.RoleEntity;
import com.ecommerce.models.enums.RoleTypes;

public interface RoleRepo extends JpaRepository<RoleEntity, Long>{
    public Optional<RoleEntity> findByName(RoleTypes name);
}

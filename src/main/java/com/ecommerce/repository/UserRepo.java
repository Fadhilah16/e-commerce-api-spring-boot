package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByUsername(String username);
	public Boolean existsByUsername(String username);
}

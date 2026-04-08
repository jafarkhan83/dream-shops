package com.firstspringproject.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstspringproject.dream_shops.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByEmail(String email);
}

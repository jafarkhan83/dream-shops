package com.firstspringproject.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstspringproject.dream_shops.model.Cartitem;

public interface CartitemRepository extends JpaRepository<Cartitem, Long>{

    public void deleteAllByCartId(Long id);
    
}

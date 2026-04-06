package com.firstspringproject.dream_shops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstspringproject.dream_shops.model.Category;
import com.firstspringproject.dream_shops.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCategory(Category category);

    public List<Product> findByBrand(String brand);

    public List<Product> findByCategoryAndBrand(Category category, String brand);

    public List<Product> findByName(String name);

    public Long countByBrandAndName(String brand, String name);

    public List<Product> findByBrandAndName(String brand, String name);
}

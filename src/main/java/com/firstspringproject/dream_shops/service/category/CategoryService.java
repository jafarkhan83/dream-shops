package com.firstspringproject.dream_shops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.firstspringproject.dream_shops.exceptions.CategoryAlreadyExists;
import com.firstspringproject.dream_shops.exceptions.CategoryNotFoundException;
import com.firstspringproject.dream_shops.model.Category;
import com.firstspringproject.dream_shops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = Optional.ofNullable(categoryRepository.findByName(name))
            .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) throw new CategoryAlreadyExists("Category already exists!");
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
            .map((oldCategory) -> {
                oldCategory.setName(category.getName());
                return categoryRepository.save(oldCategory);
            })
            .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
            .ifPresentOrElse(categoryRepository::delete, () -> {throw new CategoryNotFoundException("");});
    }
    
}

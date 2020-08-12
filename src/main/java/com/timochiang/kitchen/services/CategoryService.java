package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.entities.Dish;
import com.timochiang.kitchen.entities.DishIngredient;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.repositories.CategoryRepository;
import com.timochiang.kitchen.repositories.DishRepository;
import com.timochiang.kitchen.repositories.UserIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findLastChildren(Category parent) {
        return categoryRepository.findFirstByParentOrderByOrderDesc(parent);
    }

    public Category find(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }

    public Category save(Category category) {

        return categoryRepository.save(category);
    }
}

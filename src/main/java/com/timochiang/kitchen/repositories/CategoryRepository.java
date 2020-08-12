package com.timochiang.kitchen.repositories;

import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.entities.Dish;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    public Category findFirstByParentOrderByOrderDesc(Category parent);
}

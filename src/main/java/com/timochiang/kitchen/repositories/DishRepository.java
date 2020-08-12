package com.timochiang.kitchen.repositories;

import com.timochiang.kitchen.entities.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish, Integer> {
}

package com.timochiang.kitchen.repositories;

import com.timochiang.kitchen.entities.UserIngredient;
import org.springframework.data.repository.CrudRepository;

public interface UserIngredientRepository extends CrudRepository<UserIngredient, Integer> {
}

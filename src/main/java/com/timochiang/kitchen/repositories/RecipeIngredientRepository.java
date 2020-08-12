package com.timochiang.kitchen.repositories;

import com.timochiang.kitchen.entities.RecipeIngredient;
import org.springframework.data.repository.CrudRepository;

public interface RecipeIngredientRepository extends CrudRepository<RecipeIngredient, Integer> {
}

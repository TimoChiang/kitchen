package com.timochiang.kitchen.repositories;

import com.timochiang.kitchen.entities.Recipe;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value =
        "SELECT r.* FROM recipe AS r JOIN " +
        "( SELECT ri.*, NULLIF(COUNT(ui.id), 0) AS available FROM recipe_ingredient ri " +
        "LEFT JOIN user_ingredient ui ON ri.category_id = ui.category_id AND ri.quantity <= ui.quantity " +
        "GROUP BY ri.id" +
        ") AS rii ON r.id = rii.recipe_id " +
        "GROUP BY r.id HAVING COUNT(rii.available) - COUNT(rii.id) = 0;", nativeQuery = true)
    Iterable<Recipe> getAllAvailableRecipes();

}

package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.Recipe;
import com.timochiang.kitchen.entities.RecipeIngredient;
import com.timochiang.kitchen.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Iterable<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Recipe find(Integer id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    public Iterable<Recipe> getAllAvailableRecipes() {
        return recipeRepository.getAllAvailableRecipes();
    }

    public Recipe findWithUserIngredient(Integer id) {
        Recipe recipe = find(id);
        for(RecipeIngredient ri : recipe.getIngredients()) {
            ri.setUserIngredients(ri.getCategory().getUserIngredients());
        }
        return recipe;
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe create(Recipe recipe) {
        // for safely remove null ingredients
        Iterator<RecipeIngredient> i = recipe.getIngredients().iterator();
        while (i.hasNext()) {
            RecipeIngredient ingredient = i.next(); // must be called before you can call i.remove()
            if (ingredient.getQuantity() != null && ingredient.getQuantity() != 0) {
                ingredient.setRecipe(recipe);
            } else {
                i.remove();
            }
        }
        return this.save(recipe);
    }

}


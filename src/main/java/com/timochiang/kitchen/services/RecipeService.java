package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.*;
import com.timochiang.kitchen.repositories.DishRepository;
import com.timochiang.kitchen.repositories.RecipeRepository;
import com.timochiang.kitchen.repositories.UserIngredientRepository;
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

}


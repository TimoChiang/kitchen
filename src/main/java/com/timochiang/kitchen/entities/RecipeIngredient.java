package com.timochiang.kitchen.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class RecipeIngredient extends AbstractIngredient {

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Transient
    private List<UserIngredient> userIngredients;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<UserIngredient> getUserIngredients() {
        return userIngredients;
    }

    public void setUserIngredients(List<UserIngredient> userIngredients) {
        this.userIngredients = userIngredients;
    }
}

package com.timochiang.kitchen.utils.dto;

import com.timochiang.kitchen.entities.UserIngredient;
import java.util.List;

public class UserIngredientDto {
    private List<UserIngredient> ingredients;
    public List<UserIngredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<UserIngredient> clients) {
        this.ingredients = clients;
    }
}

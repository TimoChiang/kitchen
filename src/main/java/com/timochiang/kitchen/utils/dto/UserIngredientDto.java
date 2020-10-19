package com.timochiang.kitchen.utils.dto;

import com.timochiang.kitchen.entities.UserIngredient;
import lombok.Data;
import java.util.List;

@Data
public class UserIngredientDto {
    private List<UserIngredient> ingredients;
}

package com.timochiang.kitchen.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class RecipeIngredient extends AbstractIngredient {
    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Transient
    private List<UserIngredient> userIngredients;
}

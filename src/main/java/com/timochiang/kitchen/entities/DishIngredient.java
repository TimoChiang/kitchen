package com.timochiang.kitchen.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class DishIngredient extends AbstractIngredient {
    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Transient
    private Integer userIngredientId;
}

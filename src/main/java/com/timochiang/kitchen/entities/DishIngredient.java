package com.timochiang.kitchen.entities;

import javax.persistence.*;

@Entity
public class DishIngredient extends AbstractIngredient {

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Transient
    private Integer userIngredientId;

    public Integer getUserIngredientId() {
        return userIngredientId;
    }

    public void setUserIngredientId(Integer userIngredientId) {
        this.userIngredientId = userIngredientId;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}

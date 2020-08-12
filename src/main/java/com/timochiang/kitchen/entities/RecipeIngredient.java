package com.timochiang.kitchen.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class RecipeIngredient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "DOUBLE(6,2) UNSIGNED")
    private Double quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false, insertable= false, updatable= false)
    private List<UserIngredient> userIngredients;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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

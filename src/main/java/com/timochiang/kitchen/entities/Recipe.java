package com.timochiang.kitchen.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(updatable = false, columnDefinition="DATETIME")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition="DATETIME")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recipe")
    private List<RecipeIngredient> ingredients;

    public void setIngredients(List<RecipeIngredient> ingredients) {
        for (RecipeIngredient ingredient : ingredients) {
            ingredient.setRecipe(this);
        }
        this.ingredients = ingredients;
    }
}

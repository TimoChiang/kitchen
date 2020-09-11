package com.timochiang.kitchen;

import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.entities.Recipe;
import com.timochiang.kitchen.entities.RecipeIngredient;
import com.timochiang.kitchen.entities.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecipeTest {
    @Autowired
    private TestEntityManager testEntityManager;
    private Recipe r;
    private RecipeIngredient ri1;
    private RecipeIngredient ri2;

    @BeforeEach
    public void setUp() {

        Category c = new Category();
        c.setName("category");
        Category category = this.testEntityManager.persistAndFlush(c);
        r = new Recipe();
        r.setName("My Recipe");
        r.setDescription("Recipe Description");

        // required columns from AbstractIngredient
        ri1 = new RecipeIngredient();
        ri1.setName("pork");
        ri1.setCategory(category);
        ri1.setQuantity(1.0);
        ri1.setUnit(Unit.PIECE);
        ri2 = new RecipeIngredient();
        ri2.setName("salt");
        ri2.setCategory(category);
        ri2.setQuantity(5.0);
        ri1.setUnit(Unit.GRAM);
    }

    @Test
    public void saveRecipe() {
        Recipe r = this.testEntityManager.persistAndFlush(this.r);
        assertThat(r.getName()).isEqualTo(this.r.getName());
        assertThat(r.getDescription()).isEqualTo(this.r.getDescription());
    }

    @Test
    public void saveRecipeIngredient() {
        List<RecipeIngredient> list = new ArrayList<>();
        list.add(ri1);
        list.add(ri2);
        this.r.setIngredients(list);
        Recipe r = this.testEntityManager.persistAndFlush(this.r);

        List<RecipeIngredient> ingredients = r.getIngredients();
        assertNotNull(ingredients);
        assertThat(ingredients.size()).isEqualTo(2);
        for (RecipeIngredient ri : ingredients) {
            assertNotNull(ri);
            assertTrue(ri.getQuantity() >= 0);
        }
    }
}

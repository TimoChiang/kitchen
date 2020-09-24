package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.entities.Recipe;
import com.timochiang.kitchen.entities.RecipeIngredient;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RecipeServiceTest {
    @Autowired
    private RecipeService recipeService;

    @MockBean
    private RecipeRepository recipeRepository;

    private final Recipe recipe1 = new Recipe();
    private List<Recipe> recipes;

    @BeforeEach
    public void setUp() {
        recipe1.setId(1);
        recipe1.setName("first recipe");
        Recipe recipe2 = new Recipe();
        recipe2.setId(2);
        recipe2.setName("second recipe");
        Recipe recipe3 = new Recipe();
        recipe3.setId(3);
        recipe3.setName("third recipe");

        recipes = new ArrayList<>(Arrays.asList(recipe1 , recipe2, recipe3));

        Category c1 = new Category();
        c1.setId(1);
        c1.setName("cat1");
        Category c2 = new Category();
        c1.setId(2);
        c1.setName("cat2");
        UserIngredient ui1 = new UserIngredient();
        ui1.setId(1);
        ui1.setName("ui1");
        ui1.setQuantity(2.0);
        ui1.setCategory(c1);
        UserIngredient ui2 = new UserIngredient();
        ui2.setId(2);
        ui2.setName("ui2");
        ui2.setQuantity(3.0);
        ui2.setCategory(c2);
        UserIngredient ui3 = new UserIngredient();
        ui3.setId(3);
        ui3.setName("ui3");
        ui3.setQuantity(1.0);
        ui3.setCategory(c1);

        c1.setUserIngredients(new ArrayList<>(Arrays.asList(ui1,ui3)));
        c2.setUserIngredients(new ArrayList<>(Collections.singletonList(ui2)));

        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.setId(1);
        ri1.setName("ri1");
        ri1.setQuantity(2.0);
        ri1.setCategory(c1);
        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.setId(2);
        ri2.setName("ri2");
        ri2.setQuantity(5.0);
        ri2.setCategory(c2);
        RecipeIngredient ri3 = new RecipeIngredient();
        ri3.setId(3);
        ri3.setName("ri3");
        ri3.setQuantity(0.0);
        ri3.setCategory(c1);
        recipe1.setIngredients(new ArrayList<>(Arrays.asList(ri1, ri2, ri3)));

        // mock prepare
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        Mockito.when(recipeRepository.findById(recipe1.getId())).thenReturn(Optional.of(recipe1));
        Mockito.when(recipeRepository.getAllAvailableRecipes()).thenReturn(recipes);
        Mockito.when(recipeRepository.save(recipe1)).thenReturn(recipe1);
    }

    @Test
    public void findAll() {
        Iterable<Recipe> cs = recipeService.findAll();
        assertThat(((Collection<?>) cs).size()).isEqualTo(recipes.size());
        int i = 0;
        for (Recipe c : cs) {
            Recipe expect = recipes.get(i);
            assertThat(c).isEqualTo(expect);
            i++;
        }
    }

    @Test
    public void find() {
        Recipe c = recipeService.find(recipe1.getId());
        assertThat(c.getId()).isEqualTo(recipe1.getId());
        assertThat(c.getName()).isEqualTo(recipe1.getName());
    }

    @Test
    public void save() {
        Recipe recipe1 = recipeService.save(this.recipe1);
        assertThat(recipe1).isEqualTo(this.recipe1);
    }

    @Test
    public void getAllAvailableRecipes() {
        Iterable<Recipe> cs = recipeService.findAll();
        assertThat(((Collection<?>) cs).size()).isEqualTo(recipes.size());
        int i = 0;
        for (Recipe c : cs) {
            Recipe expect = recipes.get(i);
            assertThat(c).isEqualTo(expect);
            i++;
        }
    }

    @Test
    public void findWithUserIngredient() {
        Recipe recipe = recipeService.findWithUserIngredient(recipe1.getId());
        for(RecipeIngredient ri : recipe.getIngredients()) {
            assertThat(ri.getUserIngredients()).isNotNull();
            assertThat(ri.getUserIngredients().size()).isEqualTo(ri.getCategory().getUserIngredients().size());
        }
    }

    @Test
    public void create() {
        recipeService.create(recipe1);
        for(RecipeIngredient ri : recipe1.getIngredients()) {
            assertTrue("RecipeIngredient's quantity should greater than 0", ri.getQuantity() > 0);
            assertThat(ri.getRecipe()).isEqualTo(recipe1);
        }
    }
}

package com.timochiang.kitchen.controllers;

import com.timochiang.kitchen.RecipeController;
import com.timochiang.kitchen.entities.*;
import com.timochiang.kitchen.services.CategoryService;
import com.timochiang.kitchen.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private RecipeService recipeService;

    private final Recipe recipe1 = new Recipe();

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

        List<Recipe> recipes = new ArrayList<>(Arrays.asList(recipe1, recipe2, recipe3));

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
        ui1.setUnit(Unit.GRAM);
        ui1.setCategory(c1);
        UserIngredient ui2 = new UserIngredient();
        ui2.setId(2);
        ui2.setName("ui2");
        ui2.setQuantity(3.0);
        ui2.setUnit(Unit.MILLILITER);
        ui2.setCategory(c2);
        UserIngredient ui3 = new UserIngredient();
        ui3.setId(3);
        ui3.setName("ui3");
        ui3.setQuantity(1.0);
        ui3.setUnit(Unit.GRAM);
        ui3.setCategory(c1);

        c1.setUserIngredients(new ArrayList<>(Arrays.asList(ui1,ui3)));
        c2.setUserIngredients(new ArrayList<>(Collections.singletonList(ui2)));
        List<Category> categories = new ArrayList<>(Arrays.asList(c1, c2));

        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.setId(1);
        ri1.setName("ri1");
        ri1.setQuantity(2.0);
        ri1.setCategory(c1);
        ri1.setUnit(Unit.GRAM);
        ri1.setUserIngredients(ri1.getCategory().getUserIngredients());
        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.setId(2);
        ri2.setName("ri2");
        ri2.setQuantity(5.0);
        ri2.setCategory(c2);
        ri2.setUnit(Unit.MILLILITER);
        ri2.setUserIngredients(ri2.getCategory().getUserIngredients());
        recipe1.setIngredients(new ArrayList<>(Arrays.asList(ri1, ri2)));

        // mock prepare
        Mockito.when(categoryService.findAll()).thenReturn(categories);

        Mockito.when(recipeService.findAll()).thenReturn(recipes);
        Mockito.when(recipeService.findWithUserIngredient(recipe1.getId())).thenReturn(recipe1);
        Mockito.when(recipeService.getAllAvailableRecipes()).thenReturn(recipes);
        Mockito.when(recipeService.create(recipe1)).thenReturn(recipe1);
    }

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("レシピ一覧"))); // check template render
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(get("/recipe/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("レシピを追加")));
    }

    @Test
    public void store() throws Exception {
        MockHttpServletRequestBuilder store = post("/recipe/create")
                .param("name", recipe1.getName());
        for (int i = 0; i < recipe1.getIngredients().size(); i++) {
            store.param("ingredients["+i+"].category.id", Integer.toString(recipe1.getIngredients().get(i).getId()))
                    .param("ingredients["+i+"].name", recipe1.getIngredients().get(i).getName())
                    .param("ingredients["+i+"].quantity", Double.toString(recipe1.getIngredients().get(i).getQuantity()));
        }
        mockMvc.perform(store)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipe"));
    }

    @Test
    public void show() throws Exception {
        mockMvc.perform(get("/recipe/" + recipe1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(recipe1.getName())));
    }
}

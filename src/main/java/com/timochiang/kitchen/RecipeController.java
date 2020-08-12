package com.timochiang.kitchen;

import com.timochiang.kitchen.entities.Recipe;
import com.timochiang.kitchen.entities.RecipeIngredient;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("")
    public String index(Model model, @RequestParam(defaultValue = "false") Boolean available) {
        model.addAttribute("recipes", available ? recipeService.getAllAvailableRecipes() : recipeService.findAll());
        return "recipe/index";
    }

    @GetMapping("{recipeId}")
    public String show(Model model, @PathVariable("recipeId") Integer recipeId) {
        model.addAttribute("recipe", recipeService.find(recipeId));
        return "recipe/show";
    }
}

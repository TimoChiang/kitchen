package com.timochiang.kitchen;

import com.timochiang.kitchen.entities.Recipe;
import com.timochiang.kitchen.entities.Unit;
import com.timochiang.kitchen.services.CategoryService;
import com.timochiang.kitchen.services.RecipeService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final CategoryService categoryService;

    public RecipeController(@NonNull RecipeService recipeService, @NonNull CategoryService categoryService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String index(Model model, @RequestParam(defaultValue = "false") Boolean available) {
        model.addAttribute("recipes", available ? recipeService.getAllAvailableRecipes() : recipeService.findAll());
        return "recipe/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("units", Unit.values());
        model.addAttribute("categories", categoryService.findAll());
        return "recipe/create";
    }

    @PostMapping("create")
    public String store(Recipe recipe) {
        Recipe r = recipeService.create(recipe);
        return "redirect:/recipe";
    }

    @GetMapping("{recipeId}")
    public String show(Model model, @PathVariable("recipeId") Integer recipeId) {
        Recipe r = recipeService.findWithUserIngredient(recipeId);
        model.addAttribute("recipe", r);
        return "recipe/show";
    }
}

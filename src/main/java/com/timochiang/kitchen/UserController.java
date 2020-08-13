package com.timochiang.kitchen;

import com.timochiang.kitchen.entities.Dish;
import com.timochiang.kitchen.entities.Unit;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.services.CategoryService;
import com.timochiang.kitchen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/ingredient")
    public String ingredient(Model model) {
        model.addAttribute("ingredients", userService.findAllIngredient());
        model.addAttribute("units", Unit.values());
        model.addAttribute("categories", categoryService.findAll());
        return "user/ingredient";
    }

    @PostMapping("/ingredient")
    public String createIngredient(UserIngredient ingredient) {
        ingredient.setOriginalQuantity(ingredient.getQuantity());
        userService.saveIngredient(ingredient);
        return "redirect:/user/ingredient";
    }

    @GetMapping("/dish")
    public String dish(Model model) {
        model.addAttribute("ingredients", userService.findAllIngredient());
        return "user/dish";
    }

    @PostMapping("/dish")
    public String createDish(Dish dish) {
        userService.createDish(dish);
        return "redirect:/user/dish/history";
    }

    @GetMapping("/dish/history")
    public String dishHistory(Model model) {
        model.addAttribute("dishs", userService.findAllDish());
        return "user/dish_history";
    }

}

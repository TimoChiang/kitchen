package com.timochiang.kitchen;

import com.timochiang.kitchen.entities.Dish;
import com.timochiang.kitchen.entities.Unit;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.services.CategoryService;
import com.timochiang.kitchen.services.UserService;
import com.timochiang.kitchen.utils.dto.UserIngredientDto;
import com.timochiang.kitchen.utils.json.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/ingredient/receipt")
    public String ingredientFromReceipt(Model model) {
        return "user/ingredient_receipt";
    }

    @PostMapping("/ingredient/receipt")
    public String createIngredientFromReceipt(@RequestParam("receipt_image") MultipartFile file, RedirectAttributes redirectAttributes) {
        List<Product> ingredients = new ArrayList<>();
        String errorMessage = "";
        try {
            ingredients = userService.uploadReceipt(file);
            if (ingredients.size() == 0) {
                errorMessage = "スキャンの結果はゼロです。";
            }
            for(Product p : ingredients) {
                System.out.println(p.getName());
            }
        } catch (IllegalArgumentException e) {
            errorMessage = "写真が取得できませんでした。";
        } catch (IOException | ResourceAccessException e) {
            errorMessage = "スキャン機能はメンテナンス実施中。";
        }

        if (errorMessage.isEmpty()) {
            redirectAttributes.addFlashAttribute("ingredients", ingredients);
            return "redirect:/user/ingredient/receipt/confirm";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/user/ingredient/receipt";
        }
    }

    @GetMapping("/ingredient/receipt/confirm")
    public String ingredientFromReceiptConfirm(Model model,
                                               @ModelAttribute("ingredients") final Product[] ingredients) throws IOException {
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("units", Unit.values());
        model.addAttribute("categories", categoryService.findAll());
        return "user/ingredient_receipt_confirm";
    }

    @PostMapping("/ingredient/bulk")
    public String createIngredientBulk(@ModelAttribute UserIngredientDto dto) {
        for(UserIngredient ingredient : dto.getIngredients()) {
            if (ingredient.getCategory() != null ){
                ingredient.setOriginalQuantity(ingredient.getQuantity());
                userService.saveIngredient(ingredient);
            }
        }
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

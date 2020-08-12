package com.timochiang.kitchen;

import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/create";
    }

    @PostMapping("")
    public String store(Category category) {
        if (category.getParent() != null) {
            Category lastChildren = categoryService.findLastChildren(category.getParent());
            category.setOrder(lastChildren.getOrder() + 1);
        }
        Category c = categoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("{categoryId}")
    public String show(Model model, @PathVariable("categoryId") Integer categoryId) {
        model.addAttribute("category", categoryService.find(categoryId));
        return "category/show";
    }

    @GetMapping("edit/{categoryId}")
    public String edit(Model model, @PathVariable("categoryId") Integer categoryId) {
        model.addAttribute("category", categoryService.find(categoryId));
        model.addAttribute("categories", categoryService.findAll());
        return "category/edit";
    }

    @PutMapping("")
    public String update(Model model) {
        return "create";
    }
}

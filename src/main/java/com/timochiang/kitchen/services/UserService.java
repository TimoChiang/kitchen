package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.Dish;
import com.timochiang.kitchen.entities.DishIngredient;
import com.timochiang.kitchen.entities.UserIngredient;
import com.timochiang.kitchen.repositories.DishRepository;
import com.timochiang.kitchen.repositories.UserIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;

@Service
public class UserService {
    @Autowired
    private UserIngredientRepository userIngredientRepository;
    @Autowired
    private DishRepository dishRepository;

    public Iterable<UserIngredient> findAllIngredient() {
        return userIngredientRepository.findAll();
    }

    public UserIngredient findIngredientById(Integer id) {
        return userIngredientRepository.findById(id).orElseThrow();
    }

    public UserIngredient saveIngredient(UserIngredient ingredient) {
        return userIngredientRepository.save(ingredient);
    }

    public Iterable<Dish> findAllDish() {
        return dishRepository.findAll();
    }

    public Dish saveDish(Dish dish) {
        dish.setScheduleDateTime();
        return dishRepository.save(dish);
    }

    @Transactional
    public Dish createDish(Dish dish) {
        // for safely remove null ingredients
        Iterator<DishIngredient> i = dish.getIngredients().iterator();
        while (i.hasNext()) {
            DishIngredient ingredient = i.next(); // must be called before you can call i.remove()
            if (ingredient.getUserIngredientId() != null && ingredient.getQuantity() != null && ingredient.getQuantity() > 0) {
                UserIngredient userIngredient = this.findIngredientById(ingredient.getUserIngredientId());
                if (ingredient.getQuantity() <= userIngredient.getQuantity()) {
                    userIngredient.setQuantity(userIngredient.getQuantity() - ingredient.getQuantity());
                } else {
                    // maximum quantity follows user ingredient's maximum quantity
                    ingredient.setQuantity(userIngredient.getQuantity());
                    userIngredient.setQuantity(0.0);
                }
                this.saveIngredient(userIngredient);
                this.setDataFromIngredient(ingredient, userIngredient);
                ingredient.setDish(dish);
            } else {
                i.remove();
            }
        }
        return this.saveDish(dish);
    }

    private void setDataFromIngredient(DishIngredient i, UserIngredient a) {
        i.setName(a.getName());
        i.setUnit(a.getUnit());
        i.setCategory(a.getCategory());
    }
}

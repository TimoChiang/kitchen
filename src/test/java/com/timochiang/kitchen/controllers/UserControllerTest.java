package com.timochiang.kitchen.controllers;

import com.timochiang.kitchen.UserController;
import com.timochiang.kitchen.entities.*;
import com.timochiang.kitchen.services.CategoryService;
import com.timochiang.kitchen.services.UserService;
import com.timochiang.kitchen.utils.json.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

    private final UserIngredient ui1 = new UserIngredient();
    private final Dish dish1 = new Dish();
    private List<Dish> dishes;
    private MockMultipartFile file;

    @BeforeEach
    public void setUp() throws IOException {
        Category c1 = new Category();
        c1.setId(1);
        c1.setName("cat1");
        Category c2 = new Category();
        c1.setId(2);
        c1.setName("cat2");
        ui1.setId(1);
        ui1.setName("first user ingredient");
        ui1.setQuantity(10.0);
        ui1.setOriginalQuantity(10.0);
        ui1.setCategory(c1);
        ui1.setUnit(Unit.GRAM);
        UserIngredient ui2 = new UserIngredient();
        ui2.setId(2);
        ui2.setName("second user ingredient");
        ui2.setQuantity(3.0);
        ui2.setOriginalQuantity(3.0);
        ui2.setCategory(c2);
        ui2.setUnit(Unit.MILLILITER);
        UserIngredient ui3 = new UserIngredient();
        ui3.setId(3);
        ui3.setName("third user ingredient");
        ui3.setQuantity(1.0);
        ui3.setOriginalQuantity(1.0);
        ui3.setCategory(c1);
        ui3.setUnit(Unit.PIECE);


        c1.setUserIngredients(new ArrayList<>(Arrays.asList(ui1,ui3)));
        c2.setUserIngredients(new ArrayList<>(Collections.singletonList(ui2)));
        List<UserIngredient> userIngredients = new ArrayList<>(Arrays.asList(ui1, ui2, ui3));
        List<Category> categories = new ArrayList<>(Arrays.asList(c1, c2));

        dish1.setId(1);
        dish1.setName("first dish");
        dish1.setScheduleDate("2020-01-08");
        dish1.setScheduleTime("10");
        Dish dish2 = new Dish();
        dish2.setId(2);
        dish2.setName("second dish");
        Dish dish3 = new Dish();
        dish3.setId(3);
        dish3.setName("third recipe");
        dishes = new ArrayList<>(Arrays.asList(dish1 , dish2, dish3));

        DishIngredient di1 = new DishIngredient();
        di1.setId(1);
        di1.setQuantity(2.0);
        di1.setUserIngredientId(ui1.getId());
        DishIngredient di2 = new DishIngredient();
        di2.setId(2);
        di2.setQuantity(3.0);
        di2.setUserIngredientId(ui2.getId());
        dish1.setIngredients(new ArrayList<>(Arrays.asList(di1, di2)));

        FileInputStream inputStream = new FileInputStream("src/test/resources/files/test.jpg");
        file = new MockMultipartFile("receipt_image", "test.jpg", "image/jpg", inputStream);

        // mock prepare
        Mockito.when(categoryService.findAll()).thenReturn(categories);
        Mockito.when(userService.findAllIngredient()).thenReturn(userIngredients);
        Mockito.when(userService.createDish(dish1)).thenReturn(dish1);
        Mockito.when(userService.findAllDish()).thenReturn(dishes);
    }

    @Test
    public void ingredient() throws Exception {
        mockMvc.perform(get("/user/ingredient"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("お手元の食材を追加")))
                .andExpect(content().string(containsString("現在の食材")));
    }

    @Test
    public void createIngredient() throws Exception {
        MockHttpServletRequestBuilder createIngredient = post("/user/ingredient")
                .param("name", ui1.getName())
                .param("category.id", Integer.toString(ui1.getCategory().getId()))
                .param("quantity", Double.toString(ui1.getQuantity()))
                .param("unit", ui1.getUnit().name());
        mockMvc.perform(createIngredient)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/ingredient"));
    }

    @Test
    public void dish() throws Exception {
        mockMvc.perform(get("/user/dish"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("料理を追加")));

    }

    @Test
    public void createDish() throws Exception {
        MockHttpServletRequestBuilder createDish = post("/user/dish")
                .param("name", dish1.getName());
        for (int i = 0; i < dish1.getIngredients().size(); i++) {
            createDish.param("ingredients["+i+"].name", dish1.getIngredients().get(i).getName())
                    .param("ingredients["+i+"].quantity", Double.toString(dish1.getIngredients().get(i).getQuantity()))
                    .param("ingredients["+i+"].userIngredientId", Integer.toString(dish1.getIngredients().get(i).getUserIngredientId()));
        }
        mockMvc.perform(createDish)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dish/history"));
    }

    @Test
    public void dishHistory() throws Exception {
        ResultActions dishHistory = mockMvc.perform(get("/user/dish/history")).andExpect(status().isOk());
        for (Dish d : dishes) {
            dishHistory.andExpect(content().string(containsString(d.getName())));
        }
    }

    @Test
    public void ingredientFromReceipt() throws Exception {
        mockMvc.perform(get("/user/ingredient/receipt"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("写真から食材を追加")));

    }

    @Test
    public void createIngredientFromReceipt() throws Exception {
        Product p1 = new Product();
        p1.setName("test product 1");
        p1.setQuantity(3);
        p1.setPrice(100);
        p1.setDiscount(0);
        Product p2 = new Product();
        p2.setName("test product 2");
        p2.setQuantity(1);
        p2.setPrice(1000);
        p2.setDiscount(100);
        List<Product> products = new ArrayList<>(Arrays.asList(p1, p2));

        Mockito.when(userService.uploadReceipt(file)).thenReturn(products);

        MockHttpServletRequestBuilder createIngredient = multipart("/user/ingredient/receipt")
                .file(file);
        mockMvc.perform(createIngredient)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/ingredient/receipt/confirm"));
    }

    @Test
    public void createIngredientWithZeroItem() throws Exception {
        List<Product> products = new ArrayList<>();
        Mockito.when(userService.uploadReceipt(file)).thenReturn(products);
        MockHttpServletRequestBuilder createIngredient = multipart("/user/ingredient/receipt")
                .file(file);
        mockMvc.perform(createIngredient)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/ingredient/receipt"));
    }
}

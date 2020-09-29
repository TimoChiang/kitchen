package com.timochiang.kitchen.controllers;

import com.timochiang.kitchen.CategoryController;
import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.services.CategoryService;
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
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private Category parent;
    private Category child2;

    @BeforeEach
    public void setUp() {
        parent = new Category();
        parent.setId(1);
        parent.setName("first category");
        Category child1 = new Category();
        child1.setId(2);
        child1.setName("second category");
        child1.setOrder(0);
        child1.setParent(parent);
        child2 = new Category();
        child2.setId(3);
        child2.setName("third category");
        child2.setOrder(1);
        child2.setParent(parent);

        List<Category> categories = new ArrayList<>(Arrays.asList(parent, child1, child2));

        // mock prepare
        Mockito.when(categoryService.findAll()).thenReturn(categories);
        Mockito.when(categoryService.find(parent.getId())).thenReturn(parent);
        Mockito.when(categoryService.findLastChildren(Mockito.any(Category.class))).thenReturn(child2);
        Mockito.when(categoryService.save(parent)).thenReturn(parent);
        Mockito.when(categoryService.save(child2)).thenReturn(child2);
    }

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/category"))
//                .andDo(print()) // debug
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("食材分類一覧"))); // check template render
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(get("/category/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(parent.getName())));
    }

    @Test
    public void store() throws Exception {
        MockHttpServletRequestBuilder createParentCategory = post("/category")
                .param("name", parent.getName());
        mockMvc.perform(createParentCategory)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category"));

        MockHttpServletRequestBuilder createChildCategory = post("/category")
                .param("name", child2.getName())
                .param("parent.id", Integer.toString(child2.getParent().getId()));
        mockMvc.perform(createChildCategory)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category"));
    }

    @Test
    public void show() throws Exception {
        mockMvc.perform(get("/category/" + parent.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(parent.getName())));
    }

    @Test
    public void edit() throws Exception {
        mockMvc.perform(get("/category/edit/" + parent.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(parent.getName())))
                .andExpect(content().string(containsString(child2.getName())));
    }
}

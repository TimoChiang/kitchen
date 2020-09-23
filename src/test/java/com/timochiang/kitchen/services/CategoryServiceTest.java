package com.timochiang.kitchen.services;

import com.timochiang.kitchen.entities.Category;
import com.timochiang.kitchen.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category parent;
    private Category child1;
    private Category child2;
    private List<Category> categories;

    @BeforeEach
    public void setUp() {
        parent = new Category();
        parent.setId(1);
        parent.setName("first category");
        child1 = new Category();
        child1.setId(2);
        child1.setName("second category");
        child1.setOrder(0);
        child1.setParent(parent);
        child2 = new Category();
        child2.setId(3);
        child2.setName("third category");
        child2.setOrder(1);
        child2.setParent(parent);

        categories = new ArrayList<>();
        categories.add(parent);
        categories.add(child1);
        categories.add(child2);

        // mock prepare
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        Mockito.when(categoryRepository.findById(parent.getId())).thenReturn(Optional.of(parent));
        Mockito.when(categoryRepository.findFirstByParentOrderByOrderDesc(parent)).thenReturn(child2);
        Mockito.when(categoryRepository.save(parent)).thenReturn(parent);
    }

    @Test
    public void findAll() {
        Iterable<Category> cs = categoryService.findAll();
        assertThat(((Collection<?>) cs).size()).isEqualTo(categories.size());
        int i = 0;
        for (Category c : cs) {
            Category expect = categories.get(i);
            assertThat(c).isEqualTo(expect);
            i++;
        }
    }

    @Test
    public void findLastChildren() {
        Category c = categoryService.findLastChildren(parent);
        assertThat(c.getId()).isEqualTo(child2.getId());
        assertThat(c.getName()).isEqualTo(child2.getName());
    }

    @Test
    public void find() {
        Category c = categoryService.find(parent.getId());
        assertThat(c.getId()).isEqualTo(parent.getId());
        assertThat(c.getName()).isEqualTo(parent.getName());
    }

    @Test
    public void save() {
        Category parent = categoryService.save(this.parent);
        assertThat(parent).isEqualTo(this.parent);
    }
}

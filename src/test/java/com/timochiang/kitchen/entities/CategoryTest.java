package com.timochiang.kitchen.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CategoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    private Category parent;
    private Category child1;
    private Category child2;

    @BeforeEach
    public void setUp() {
        parent = new Category();
        parent.setName("Parent Category");
        child1 = new Category();
        child1.setName("child 1");
        child2 = new Category();
        child2.setName("child 2");
    }

    @Test
    public void saveCategory() {
        Category parent = this.testEntityManager.persistAndFlush(this.parent);
        assertThat(parent.getName()).isEqualTo(this.parent.getName());
    }

    @Test
    public void saveCategoryChildren() {
        Category parent = this.testEntityManager.persistAndFlush(this.parent);

        child1.setParent(parent);
        child1.setOrder(0);

        child2.setParent(parent);
        child2.setOrder(1);

        this.testEntityManager.persist(child1);
        this.testEntityManager.persist(child2);
        testEntityManager.flush();
        testEntityManager.refresh(parent);
        List<Category> children = parent.getChildren();
        assertNotNull(children);
        assertThat(children.size()).isEqualTo(2);
        for (Category child : children) {
            assertNotNull(child);
            assertTrue(child.getName().contains("child"));
        }
    }
}

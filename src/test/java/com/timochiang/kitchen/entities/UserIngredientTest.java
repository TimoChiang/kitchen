package com.timochiang.kitchen.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserIngredientTest {
    @Autowired
    private TestEntityManager testEntityManager;
    private UserIngredient ui;

    @BeforeEach
    public void setUp() {
        // required columns from AbstractIngredient
        Category c = new Category();
        c.setName("category");
        Category category = this.testEntityManager.persistAndFlush(c);
        ui = new UserIngredient();
        ui.setName("My Ingredient");
        ui.setCategory(category);
        ui.setQuantity(2.0);
        ui.setUnit(Unit.GRAM);
    }

    @Test
    public void saveUserIngredient() {
        this.ui.setOriginalQuantity(ui.getQuantity());
        UserIngredient ui = this.testEntityManager.persistAndFlush(this.ui);
        assertThat(ui.getName()).isEqualTo(this.ui.getName());
        assertThat(ui.getQuantity()).isEqualTo(this.ui.getQuantity());
        // set the originalQuantity same as quantity
        assertThat(ui.getOriginalQuantity()).isEqualTo(this.ui.getQuantity());
    }

    @Test
    public void originalQuantityShouldNotNull() {
        Exception exception = assertThrows(PersistenceException.class, () -> this.testEntityManager.persistAndFlush(this.ui));
        String actualMessage = exception.getMessage();
        String expectedMessage1 = "not-null property references";
        String expectedMessage2 = "originalQuantity";
        assertTrue(actualMessage.contains(expectedMessage1));
        assertTrue(actualMessage.contains(expectedMessage2));
    }
}

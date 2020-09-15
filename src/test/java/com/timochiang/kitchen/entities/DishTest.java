package com.timochiang.kitchen.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DishTest {
    @Autowired
    private TestEntityManager testEntityManager;
    private Dish d;
    private DishIngredient di1;
    private DishIngredient di2;

    @BeforeEach
    public void setUp() {

        Category c = new Category();
        c.setName("category");
        Category category = this.testEntityManager.persistAndFlush(c);
        d = new Dish();
        d.setName("My Dish");

        // required columns from AbstractIngredient
        di1 = new DishIngredient();
        di1.setName("pork");
        di1.setCategory(category);
        di1.setQuantity(1.0);
        di1.setUnit(Unit.PIECE);
        di2 = new DishIngredient();
        di2.setName("salt");
        di2.setCategory(category);
        di2.setQuantity(5.0);
        di1.setUnit(Unit.GRAM);
    }

    @Test
    public void saveDish() {
        Dish d = this.testEntityManager.persistAndFlush(this.d);
        assertThat(d.getName()).isEqualTo(this.d.getName());
    }

    @Test
    public void saveDishIngredient() {
        List<DishIngredient> list = new ArrayList<>();
        list.add(di1);
        list.add(di2);
        this.d.setIngredients(list);
        Dish d = this.testEntityManager.persistAndFlush(this.d);

        List<DishIngredient> ingredients = d.getIngredients();
        assertNotNull(ingredients);
        assertThat(ingredients.size()).isEqualTo(2);
        for (DishIngredient ri : ingredients) {
            assertNotNull(ri);
            assertTrue(ri.getQuantity() >= 0);
        }
    }

    @Test
    public void saveDishScheduleDateTime() throws ParseException {
        this.d.setScheduleDate("2020-01-08");
        this.d.setScheduleTime("10");
        this.d.setScheduleDateTime();
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.d.getScheduleDate() + " " + this.d.getScheduleTime() + ":00:00");
        Dish d = this.testEntityManager.persistAndFlush(this.d);
        assertEquals(d.getScheduleDateTime(), date);
    }

    @Test
    public void saveDishInvalidScheduleDateTime() {
        this.d.setScheduleDate("2020-01-08");
        this.d.setScheduleTime("10:00:00");
        Dish d = this.testEntityManager.persistAndFlush(this.d);
        assertNull(d.getScheduleDateTime());
    }
}

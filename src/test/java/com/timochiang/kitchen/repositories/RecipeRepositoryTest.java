package com.timochiang.kitchen.repositories;


import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.timochiang.kitchen.CommonOperations;
import com.timochiang.kitchen.entities.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecipeRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() {
        Operation operation =
                sequenceOf(
                        CommonOperations.INSERT_REFERENCE_DATA,
                        insertInto("recipe")
                                .columns("id", "name")
                                .values(10, "Available Recipe 1")
                                .values(11, "Unavailable Recipe")
                                .values(12, "Available Recipe 2")
                                .build(),
                        insertInto("recipe_ingredient")
                                .columns("category_id", "name", "quantity", "recipe_id")
                                .values(102, "pork", 1.0, 10)
                                .values(102, "pork", 5.0, 11)
                                .values(102, "pork", 1.0, 12)
                                .values(103, "spinach", 100.0, 12)
                                .build());
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();
    }


    @Test
    public void getCategory() {
        Iterable<Recipe> ui = recipeRepository.getAllAvailableRecipes();
        int counter = 0;
        for (Recipe r : ui) {
            counter++;
        }

        // 2 row should be found in the test case
        assertThat(counter).isEqualTo(2);
    }

}

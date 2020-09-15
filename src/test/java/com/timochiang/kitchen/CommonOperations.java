package com.timochiang.kitchen;

import com.ninja_squad.dbsetup.operation.*;

import static com.ninja_squad.dbsetup.Operations.*;

public class CommonOperations {
    public static final Operation INSERT_REFERENCE_DATA =
            sequenceOf(
                    insertInto("category")
                            .columns("id", "name", "parent_id", "children_order")
                            .values(101, "Parent Category", null, null)
                            .values(102, "Children Category 1", 101, 0)
                            .values(103, "Children Category 2", 101, 1)
                            .build(),
                    insertInto("user_ingredient")
                            .columns("category_id", "name", "quantity", "original_quantity")
                            .values(102, "good pork", 1.0, 1.0)
                            .values(103, "fresh spinach" , 300.0, 300.0)
                            .build());
}

package com.timochiang.kitchen;

import com.ninja_squad.dbsetup.operation.*;

import static com.ninja_squad.dbsetup.Operations.*;

public class CommonOperations {
    public static final Operation INSERT_REFERENCE_DATA =
            sequenceOf(
                    insertInto("category")
                            .columns("id", "name", "parent_id", "children_order")
                            .values(1, "Parent Category", null, null)
                            .values(2, "Children Category 1", 1, 0)
                            .values(3, "Children Category 2", 1, 1)
                            .build(),
                    insertInto("user_ingredient")
                            .columns("category_id", "name", "quantity", "original_quantity")
                            .values(2, "good pork", 1.0, 1.0)
                            .values(3, "fresh spinach" , 300.0, 300.0)
                            .build());
}

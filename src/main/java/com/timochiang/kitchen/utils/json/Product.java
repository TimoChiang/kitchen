package com.timochiang.kitchen.utils.json;

import lombok.Data;

@Data
public class Product {
    private String name;
    private int quantity;
    private int price;
    private int discount;
}

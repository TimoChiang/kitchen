package com.timochiang.kitchen.entities;

public enum Unit {

    GRAM("克"),
    MILLILITER("ml"),
    TSP("小匙"),
    TBSP("大匙"),
    PIECE("個"),
    ;

    private final String label;

    private Unit(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
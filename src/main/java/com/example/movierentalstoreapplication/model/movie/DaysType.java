package com.example.movierentalstoreapplication.model.movie;

public enum DaysType {
    ONE("One", 1),
    THREE("Three", 3),
    FIVE("Five", 5);

    DaysType(String name, int days) {
        this.name = name;
        this.days = days;
    }

    private final String name;
    private final int days;

    public String getName() {
        return name;
    }

    public int getDays() {
        return days;
    }
}
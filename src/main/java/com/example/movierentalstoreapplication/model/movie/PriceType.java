package com.example.movierentalstoreapplication.model.movie;

public enum PriceType {
    PREMIUM("Premium", 4),
    BASIC("Basic", 3);

    PriceType(String name, int price) {
        this.name = name;
        this.price = price;
    }

    private final String name;
    private final int price;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
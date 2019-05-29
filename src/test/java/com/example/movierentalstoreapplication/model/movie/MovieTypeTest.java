package com.example.movierentalstoreapplication.model.movie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTypeTest {

    @Test
    void calculatePriceForTypeNew() {
        assertEquals(4, MovieType.NEW.calculatePrice(1));
        assertEquals(8, MovieType.NEW.calculatePrice(2));
        assertEquals(12, MovieType.NEW.calculatePrice(3));
    }

    @Test
    void calculatePriceForTypeRegular() {
        assertEquals(3, MovieType.REGULAR.calculatePrice(1));
        assertEquals(3, MovieType.REGULAR.calculatePrice(2));
        assertEquals(3, MovieType.REGULAR.calculatePrice(3));
        assertEquals(9, MovieType.REGULAR.calculatePrice(5));
    }

    @Test
    void calculatePriceForTypeOld() {
        assertEquals(3, MovieType.OLD.calculatePrice(1));
        assertEquals(3, MovieType.OLD.calculatePrice(3));
        assertEquals(3, MovieType.OLD.calculatePrice(5));
        assertEquals(6, MovieType.OLD.calculatePrice(6));
    }

    @Test
    void calculatePriceForTypeNewWithBonusPoints() {
        assertEquals(0, MovieType.NEW.calculatePrice(1, 25));
        assertEquals(4, MovieType.NEW.calculatePrice(2, 25));
        assertEquals(8, MovieType.NEW.calculatePrice(3, 25));
    }

    @Test
    void calculatePriceForTypeRegularWithBonusPoints() {
        assertEquals(3, MovieType.REGULAR.calculatePrice(1, 25));
        assertEquals(3, MovieType.REGULAR.calculatePrice(2, 25));
        assertEquals(3, MovieType.REGULAR.calculatePrice(3, 25));
        assertEquals(9, MovieType.REGULAR.calculatePrice(5, 25));
    }

    @Test
    void calculatePriceForTypeOldWithBonusPoints() {
        assertEquals(3, MovieType.OLD.calculatePrice(1, 25));
        assertEquals(3, MovieType.OLD.calculatePrice(3, 25));
        assertEquals(3, MovieType.OLD.calculatePrice(5, 25));
        assertEquals(6, MovieType.OLD.calculatePrice(6, 25));
    }


    @Test
    void calculatePoints() {
        assertEquals(2, MovieType.NEW.calculatePoints());
        assertEquals(1, MovieType.REGULAR.calculatePoints());
        assertEquals(1, MovieType.OLD.calculatePoints());
    }
}
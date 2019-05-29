package com.example.movierentalstoreapplication.model.movie;

public enum PointsType {
    PREMIUM("Premium", 25, 2) {
        @Override
        public int calculatePrice(int bonusPoints) {
            int pointsPrice = this.getPrice();

            if (bonusPoints >= 1 && pointsPrice >= 1) {
                int numberOfBonusDays = bonusPoints / pointsPrice;

                if (numberOfBonusDays >= 1) {
                    return numberOfBonusDays * pointsPrice;
                }
            }

            return 0;
        }

        @Override
        public int calculateBonusDays(int bonusPoints) {
            int pointsPrice = this.getPrice();

            if (bonusPoints >= 1 && pointsPrice >= 1) {
                return bonusPoints / pointsPrice;
            }

            return 0;
        }
    },
    BASIC("Basic", 0, 1) {
        @Override
        public int calculatePrice(int bonusPoints) {
            return 0;
        }

        @Override
        public int calculateBonusDays(int bonusPoints) {
            return 0;
        }
    };

    PointsType(String name, int price, int bonusPoints) {
        this.name = name;
        this.price = price;
        this.bonusPoints = bonusPoints;
    }

    private final String name;
    private final int price;
    private final int bonusPoints;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public abstract int calculatePrice(int bonusPoints);

    public abstract int calculateBonusDays(int bonusPoints);
}
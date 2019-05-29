package com.example.movierentalstoreapplication.model.movie;

public enum MovieType {
    NEW("New", PriceType.PREMIUM, PointsType.PREMIUM, DaysType.ONE) {
        @Override
        public double calculatePrice() {
            return this.calculatePrice(1);
        }

        @Override
        public double calculatePrice(int days) {
            int basePrice = this.getPriceType().getPrice();

            return (double) basePrice * days;
        }

        @Override
        public double calculatePrice(int days, int bonusPoints) {
            int basePrice = this.getPriceType().getPrice();
            int bonusPrice = basePrice * this.getPointsType().calculateBonusDays(bonusPoints);

            return this.calculatePrice(days) - bonusPrice;
        }
    },

    REGULAR("Regular", PriceType.BASIC, PointsType.BASIC, DaysType.THREE) {
        @Override
        public double calculatePrice() {
            return this.calculatePrice(1);
        }

        @Override
        public double calculatePrice(int days) {
            int numberOfFirstDays = this.getDaysType().getDays();
            int basePrice = this.getPriceType().getPrice();

            if (days > numberOfFirstDays) {
                return (double) basePrice + (basePrice * (days - numberOfFirstDays));
            }

            return basePrice;
        }

        @Override
        public double calculatePrice(int days, int bonusPoints) {
            int numberOfFirstDays = this.getDaysType().getDays();
            int basePrice = this.getPriceType().getPrice();
            int bonusPrice = basePrice * this.getPointsType().calculateBonusDays(bonusPoints);

            if (days > numberOfFirstDays) {
                return this.calculatePrice(days) - bonusPrice;
            }

            return basePrice - bonusPrice;
        }
    },

    OLD("Old", PriceType.BASIC, PointsType.BASIC, DaysType.FIVE) {
        @Override
        public double calculatePrice() {
            return this.calculatePrice(1);
        }

        @Override
        public double calculatePrice(int days) {
            int numberOfFirstDays = this.getDaysType().getDays();
            int basePrice = this.getPriceType().getPrice();

            if (days > numberOfFirstDays) {
                return (double) basePrice + (basePrice * (days - numberOfFirstDays));
            }

            return basePrice;
        }

        @Override
        public double calculatePrice(int days, int bonusPoints) {
            int numberOfFirstDays = this.getDaysType().getDays();
            int basePrice = this.getPriceType().getPrice();
            int bonusPrice = basePrice * this.getPointsType().calculateBonusDays(bonusPoints);

            if (days > numberOfFirstDays) {
                return this.calculatePrice(days) - bonusPrice;
            }

            return basePrice - bonusPrice;
        }
    };

    private String name;
    private PriceType priceType;
    private PointsType pointsType;
    private DaysType daysType;

    MovieType(String name, PriceType priceType, PointsType pointsType, DaysType daysType) {
        this.name = name;
        this.priceType = priceType;
        this.pointsType = pointsType;
        this.daysType = daysType;
    }

    public String getName() {
        return name;
    }

    public PriceType getPriceType() {
        return this.priceType;
    }

    public PointsType getPointsType() {
        return this.pointsType;
    }

    public DaysType getDaysType() {
        return this.daysType;
    }

    public int calculatePoints() {
        return this.getPointsType().getBonusPoints();
    }

    public abstract double calculatePrice();

    public abstract double calculatePrice(int days);

    public abstract double calculatePrice(int days, int bonusPoints);

}
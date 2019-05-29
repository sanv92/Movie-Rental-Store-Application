package com.example.movierentalstoreapplication.dtos;

import java.util.Objects;

public class CustomerBalanceDto {

    private Double balance;

    public Double getBalance() {
        return balance;
    }

    public CustomerBalanceDto() {}

    public CustomerBalanceDto(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerBalanceDto)) return false;
        CustomerBalanceDto that = (CustomerBalanceDto) o;
        return Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return "CustomerBalanceDto{" +
                "balance=" + balance +
                '}';
    }
}
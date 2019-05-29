package com.example.movierentalstoreapplication.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class CustomerDto {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 80)
    private String lastName;

    @Min(0)
    private Double balance = 0.0;

    @Min(0)
    private Integer bonusPoints = 0;

    public CustomerDto() {
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getBalance() {
        return balance;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public CustomerDto setId(Long id) {
        this.id = id;
        return this;
    }

    public CustomerDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerDto setBalance(Double balance) {
        this.balance = balance;
        return this;
    }

    public CustomerDto setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerDto)) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", balance=" + balance +
                ", bonusPoints=" + bonusPoints +
                '}';
    }
}

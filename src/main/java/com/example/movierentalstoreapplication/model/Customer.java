package com.example.movierentalstoreapplication.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_generator")
    @SequenceGenerator(
            name = "customer_id_generator",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    private Long id;

    @NotBlank
    @Size(min = 2, max = 40)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Min(0)
    @Column(name = "balance", nullable = false)
    private double balance = 0;

    @Min(0)
    @Column(name = "bonus_points", nullable = false)
    private int bonusPoints = 0;

    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName, double balance, int bonusPoints) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.bonusPoints = bonusPoints;
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

    public double getBalance() {
        return balance;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public Customer setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Customer setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Customer depositBalance(double balance) {
        this.balance = this.balance + balance;
        return this;
    }

    public Customer withdrawBalance(double balance) {
        this.balance = this.balance - balance;
        return this;
    }

    public Customer depositPoints(int bonusPoints) {
        this.bonusPoints = this.bonusPoints + bonusPoints;
        return this;
    }

    public Customer withdrawPoints(int bonusPoints) {
        this.bonusPoints = this.bonusPoints - bonusPoints;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                firstName.equals(customer.firstName) &&
                lastName.equals(customer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", balance=" + balance +
                ", bonusPoints=" + bonusPoints +
                '}';
    }
}

package com.example.movierentalstoreapplication.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Validator validator;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        this.customer = new Customer("First Name", "Last Name", 10, 20);
    }

    @Test
    void depositBalance() {
        customer.depositBalance(1);

        assertEquals(11, customer.getBalance());
    }

    @Test
    void withdrawBalance() {
        customer.withdrawBalance(1);

        assertEquals(9, customer.getBalance());
    }

    @Test
    void depositPoints() {
        customer.depositPoints(1);

        assertEquals(21, customer.getBonusPoints());
    }

    @Test
    void withdrawPoints() {
        customer.withdrawPoints(1);

        assertEquals(19, customer.getBonusPoints());
    }

    @Test
    void shouldHaveNoViolations() {
        Set<ConstraintViolation<Customer>> violations = validator.validate(
                new Customer("First Name", "Last Name")
        );

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldHaveNoViolationsWithBalance() {
        Set<ConstraintViolation<Customer>> violations = validator.validate(
                new Customer("First Name", "Last Name", 0, 0)
        );

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldHaveNoViolationsMin() {
        Set<ConstraintViolation<Customer>> violations = validator.validate(
                new Customer(
                        repeat(2),
                        repeat(2)
                )
        );

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldHaveNoViolationsMax() {
        Customer customer = new Customer(
                repeat(40),
                repeat(80)
        );

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldDetectInvalidFirstNameMin() {
        Customer customer = new Customer(
                repeat(1),
                repeat(2)
        );

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        ConstraintViolation<Customer> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Customer_.FIRST_NAME, violation.getPropertyPath().toString());
    }

    @Test
    void shouldDetectInvalidFirstNameMax() {
        Customer customer = new Customer(
                repeat(41),
                repeat(2)
        );

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        ConstraintViolation<Customer> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Customer_.FIRST_NAME, violation.getPropertyPath().toString());
    }

    @Test
    void shouldDetectInvalidLastNameMin() {
        Customer customer = new Customer(
                repeat(2),
                repeat(1)
        );

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        ConstraintViolation<Customer> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Customer_.LAST_NAME, violation.getPropertyPath().toString());
    }

    @Test
    void shouldDetectInvalidLastNameMax() {
        Customer customer = new Customer(
                repeat(2),
                repeat(81)
        );

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        ConstraintViolation<Customer> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Customer_.LAST_NAME, violation.getPropertyPath().toString());
    }

    @Test
    void shouldDetectInvalidBalanceAndBonus() {
        Customer customer = new Customer(
                repeat(2),
                repeat(2),
                -1,
                -1
        );

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertEquals(2, violations.size());
    }

    private static String repeat(int times) {
        return StringUtils.repeat("*", times);
    }
}
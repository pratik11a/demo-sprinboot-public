package com.example.demo.domain;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class RestaurantTest {
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant( new HashSet<>(), Optional.empty());
    }

    @Test
    void addFirstMenuInRestaurant() {
        String samosa = "Samosa";
        double price = 11.00;

        Restaurant result = restaurant.upsertMenu(new Menu(samosa, price));

        Assert.assertFalse(result.menus().isEmpty());
        Set<Menu> menus = result.menus();
        Assert.assertTrue(menus.stream().map(m -> m.name().equals(samosa)).findFirst().isPresent());

        assertEquals(price, menus.iterator().next().price(), 0.01);
    }

    @Test
    void addSecondMenuInRestaurant() {
        Restaurant result = restaurant.upsertMenu(new Menu("Samosa", 11.00));
        String secondMenu = "Kachori";
        Double price = 12.00;

        Restaurant newResult = result.upsertMenu(new Menu(secondMenu, price));

        assertEquals(2, newResult.menus().size());
        assertEquals(price, newResult.menus().stream().filter(m -> m.name().equals(secondMenu)).map(m -> m.price()).findFirst().get());
    }

    @Test
    void editExistingMenuInRestaurant() {
        Restaurant result = restaurant.upsertMenu(new Menu("Samosa", 11.00));
        String secondMenu = "Kachori";
        Double price = 12.00;
        Restaurant newResult = result.upsertMenu(new Menu(secondMenu, price));

        Double newPrice = 15.00;
        Restaurant _newResult = newResult.upsertMenu(new Menu(secondMenu, newPrice));

        assertEquals(2, _newResult.menus().size());
        assertEquals(newPrice, _newResult.menus().stream().filter(m -> m.name().equals(secondMenu)).findFirst().get().price());
    }

    @Test()
    void shouldThrowExceptionIfMenuIsNotAvailable() {
        restaurant= restaurant
                .upsertMenu(new Menu("Kachori", 11.00));
        OrderItem samosa = new OrderItem("Samosa", 1);
        UnavailableMenuException exception = assertThrows(UnavailableMenuException.class, () ->
                restaurant.orderFood(new Order(Arrays.asList(samosa))));
        assertEquals("Samosa is not available", exception.getMessage());
    }

    @Test
    void shouldOrderFood() {
        Restaurant rest = restaurant
                .upsertMenu(new Menu("Samosa", 11.00))
                .upsertMenu(new Menu("Kachori", 12.00))
                .upsertMenu(new Menu("Chat", 13.00))
                .upsertMenu(new Menu("Bhel", 14.00));

        OrderItem samosa = new OrderItem("Samosa", 2);
        Tuple restaurantAndOrder = rest.orderFood(new Order(Arrays.asList(samosa)));

        assertEquals(1, restaurantAndOrder.orderId().intValue());
        assertTrue(restaurantAndOrder.restaurant().maybeOrders().isPresent());
    }

    @Test
    void shouldReturnBillAmountForOrder() {
        String samosa = "Samosa";
        double price = 11.00;
        Restaurant rest = restaurant
                .upsertMenu(new Menu(samosa, price))
                .upsertMenu(new Menu("Kachori", 12.00))
                .upsertMenu(new Menu("Chat", 13.00))
                .upsertMenu(new Menu("Bhel", 14.00));
        int count = 2;
        OrderItem orderItem = new OrderItem(samosa, count);
        Tuple restaurantAndOrder = rest.orderFood(new Order(Arrays.asList(orderItem)));
        Restaurant restaurant1 = restaurantAndOrder.restaurant();
        Integer orderId = restaurantAndOrder.orderId();

        Optional<Double> billAmount = restaurant1.getBillAmount(orderId);

        assertEquals(Optional.of(price * 2), billAmount);
    }

    @Test
    void shouldNotReturnAmountForUnknownId() {
        Restaurant rest = restaurant
                .upsertMenu(new Menu("Kachori", 12.00))
                .upsertMenu(new Menu("Chat", 13.00))
                .upsertMenu(new Menu("Bhel", 14.00));

        Optional<Double> billAmount = rest.getBillAmount(1);
        assertEquals(Optional.empty(), billAmount);
    }
}

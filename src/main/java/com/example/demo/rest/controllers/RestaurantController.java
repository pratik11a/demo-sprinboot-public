package com.example.demo.rest.controllers;


import com.example.demo.domain.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestaurantController {

    @Autowired
    private Logger logger;
    @Autowired
    private Restaurant restaurant;

    @GetMapping("/restaurant/menu")
    public Menus  getMenu() {
        return new Menus(restaurant.menus());
    }

    @GetMapping("view/restaurant/menu")
    public String getViewMenu() {
        return "menuView";
    }

    @PostMapping("/restaurant/menu")
    public ResponseEntity<Restaurant> addMenu(@RequestBody Menu menu) {
        logger.info("{} menu added for {} price", menu.name(), menu.price());
        restaurant = restaurant.upsertMenu(menu);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PostMapping("/restaurant/order")
    public Integer order(@RequestBody Order order) {
        try {
            Tuple tuple = restaurant.orderFood(order);
            restaurant = tuple.restaurant();
            return tuple.orderId();
        } catch (NoMenuException e){
            logger.error("Cannot make an order: {}", e.getMessage());
            throw e;
        } catch (UnavailableMenuException e){
            logger.error("Menu is unavailable: {}", e.getMessage());
            throw e;
        }

    }

    @ResponseStatus(value= HttpStatus.FORBIDDEN,
            reason="Menu is not defined")  // 409
    @ExceptionHandler(NoMenuException.class)
    public void menuNotDefined() {
        // Nothing to do
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND,
            reason="Menu is not available")  // 409
    @ExceptionHandler(UnavailableMenuException.class)
    public void menuIsUnAvailable() {
        // Nothing to do
    }
}

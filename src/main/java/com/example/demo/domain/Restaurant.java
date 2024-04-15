package com.example.demo.domain;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public record Restaurant(
        Set<Menu> menus, //No default values are available
        Optional<Map<Integer, Order>> maybeOrders //No default values are available
) {

    public Restaurant upsertMenu(Menu menu) {
        Set<Menu> _menus = menus.stream().filter(m -> !m.name().equals(menu.name())).collect(Collectors.toSet());
        _menus.add(menu);
        return new Restaurant(_menus, this.maybeOrders);
    }

    private Optional<Double> price(String menuItem){
        return this.menus.stream().filter(m -> m.name().equals(menuItem)).findFirst().map(m -> m.price());
    }
    public Optional<Double> getBillAmount(Integer orderId) {
        Optional<Order> optionalOrder = maybeOrders.map(orders -> orders.get(orderId));
        return optionalOrder.map(order -> {
            return order.items().stream().map(item ->
                    price(item.name()).orElse(0.0)* item.count())
                    .toList()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
        });
    }

    public Tuple orderFood(Order order) throws NoMenuException {
        if (menus.isEmpty()) {
            throw new NoMenuException("Menu is not defined");
        }
        validateOrder(order);
        int orderId = maybeOrders.map(orders -> orders.keySet().size()).orElse(0) + 1;
        Map<Integer, Order> orders = this.maybeOrders.orElseGet(HashMap::new);
        orders.put(orderId, order);
        Restaurant restaurant = new Restaurant(this.menus, Optional.of(orders));
        return new Tuple(restaurant, orderId);
    }

    private void validateOrder(Order order) throws UnavailableMenuException {
        List<String> menuStringList = menus.stream().map(menu -> menu.name()).toList();
        order.items().forEach(orderItem -> {
            if (!menuStringList.contains(orderItem.name())){
                throw new UnavailableMenuException(orderItem.name() + " is not available");
            }
        });
    }
}

package com.example.demo.view;

import com.example.demo.domain.Menu;
import com.example.demo.domain.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RestaurantViewController {

    @Autowired
    Restaurant restaurant;
    @GetMapping("/restaurant/view/menu")
    public String getMenu(Model model) {
        model.addAttribute("menus", restaurant.menus());
        return "restaurantMenu";
    }

    @PostMapping("/restaurant/view/menu")
    public String addMenu(@RequestParam(name="menu_name", required=true) String name,
                          @RequestParam(name="menu_price", required=true) Double price,
                          Model model) {
        System.out.println("name: "+ name+ " price: "+ price);
        restaurant = restaurant.upsertMenu(new Menu(name, price));
        System.out.println(restaurant);
        model.addAttribute("menus", restaurant.menus());
        return "restaurantMenu";
    }
}

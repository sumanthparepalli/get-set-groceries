package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping({"","/"})
    public String allOrders(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model)
    {
        model.addAttribute("orders", orderService.allOrders(userDetails.getUsername()));
        return "orders";
    }

}

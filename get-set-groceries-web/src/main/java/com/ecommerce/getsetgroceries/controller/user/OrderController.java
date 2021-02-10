package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.Order;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping({"","/"})
    public String allOrders(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model)
    {
        List<Order> orders = orderService.allOrders(userDetails.getUsername());
        model.addAttribute("orders", orders)
                .addAttribute("images",orderService.getCartProductImages(orders));
        return "orders";
    }

}

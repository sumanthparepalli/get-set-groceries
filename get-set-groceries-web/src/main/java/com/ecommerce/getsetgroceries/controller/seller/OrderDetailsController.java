package com.ecommerce.getsetgroceries.controller.seller;

import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.seller.SellerOrderDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller/orders")
public class OrderDetailsController {

    private final SellerOrderDetailsService sellerOrderDetailsService;
    private final ObjectMapper objectMapper;

    public OrderDetailsController(SellerOrderDetailsService sellerOrderDetailsService, ObjectMapper objectMapper) {
        this.sellerOrderDetailsService = sellerOrderDetailsService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String getSellerOrders(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        long sellerId = userDetails.getUser().seller.getId();
        model.addAttribute("orderItems", sellerOrderDetailsService.getOrderDetails(sellerId));
        return "seller/orderDetails";
    }

    @GetMapping("/mark/{key}")
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public ResponseEntity<ObjectNode> markDelivery(@PathVariable("key") String key, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        sellerOrderDetailsService.markDelivered(key,userDetails.getUser().seller.getId());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("message","Marked delivered");
        return ResponseEntity.ok(objectNode);
    }
}

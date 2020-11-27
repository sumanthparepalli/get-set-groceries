package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.CartService;
import com.ecommerce.getsetgroceries.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Collections;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final CartService cartService;

    public PaymentController(PaymentService paymentService, ObjectMapper objectMapper, CartService cartService) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
        this.cartService = cartService;
    }

    @GetMapping("/success")
    public String success(RedirectAttributes redirectAttributes, @RequestParam(value = "credit", required = false) Boolean isCredit) {
        if (isCredit != null && isCredit)
            redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Successfully credited to seller"));
        else
            redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Order placed successfully"));
        return "redirect:/";
    }

    @PostMapping("/checkout/{callid}")
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public ObjectNode checkout(@PathVariable("callid") String callid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        int updateCount = cartService.validateCart(userDetails.getUsername());
        double cartTotal=0;
        if(updateCount>0)
        {
            cartTotal = cartService.validateCartTotal(userDetails.getUsername());
        }
        if(cartTotal<0)
        {
            objectNode.put("status", 500);
            objectNode.put("message", "Payment failed - Possibly few products ran out of stock during your purchase");
            return objectNode;
        }
        try {
            paymentService.completeOrder(callid, userDetails.getUser());
            objectNode.put("status", 200);
            objectNode.put("message", "Payment successful for "+String.valueOf(cartTotal));
        } catch (SignatureException e) {
            objectNode.put("status", 500);
            objectNode.put("message", "Error in creating payment token");
        } catch (IOException e) {
            objectNode.put("status", 500);
            objectNode.put("message", "Error in retrieving payment details");
        } catch (GenericJDBCException e) {
            objectNode.put("status", 500);
            objectNode.put("message", "Payment failed - Possibly few products ran out of stock during your purchase");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            objectNode.put("status", 500);
            objectNode.put("message", "Payment failed");
        }
        return objectNode;
    }

}

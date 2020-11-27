package com.ecommerce.getsetgroceries.controller.seller;

import com.ecommerce.getsetgroceries.models.Seller;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;

@Controller
public class DashboardController {

    @GetMapping("/seller")
    @RolesAllowed("SELLER")
    public String homeRedirect(){
        return "redirect:/seller/profile";
    }

    @GetMapping("/seller/profile")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Seller seller = user.getSeller();
        model.addAttribute("seller", seller);
        return "seller/profile";
    }

}

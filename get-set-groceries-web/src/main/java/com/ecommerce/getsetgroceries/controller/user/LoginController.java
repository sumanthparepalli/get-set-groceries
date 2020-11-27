package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.Seller;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        userService.addNewUser(user);
        ArrayList<String> flashMessages = new ArrayList<>();
        flashMessages.add("Registered Successfully! Login to Continue.");
        redirectAttributes.addFlashAttribute("flashSuccess", flashMessages);
        return "redirect:/login";
    }

    @GetMapping("/register/seller")
    public String registerSeller(Model model) {
        model.addAttribute("seller", new Seller());
        return "register-seller";
    }

    @PostMapping("/register/seller")
    public String registerSeller(Principal principal, @Valid @ModelAttribute Seller seller, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register-seller";
        }
        userService.becomeSeller(principal.getName(), seller);
        redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Login to Continue!"));
        return "redirect:/logout";
    }

}

package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.Order;
import com.ecommerce.getsetgroceries.models.UserAddress;
import com.ecommerce.getsetgroceries.repositories.UserAddressRepo;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/address")
public class AddressController {

    private final UserAddressRepo addressRepo;
    private final CartService cartService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AddressController(UserAddressRepo addressRepo, CartService cartService) {
        this.addressRepo = addressRepo;
        this.cartService = cartService;
    }

    @GetMapping({"", "/"})
    public String RedirectNew() {
        return "redirect:/address/new";
    }

    @GetMapping("/new")
    public String newAddressForm(Model model)
    {
        model.addAttribute("addr",new UserAddress());
        return "addAddress";
    }

    @PostMapping("/new")
    public String newAddress(@ModelAttribute("addr") UserAddress address, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(bindingResult.hasErrors())
        {
            return "addAddress";
        }
        address.setUser(userDetails.getUser());
        addressRepo.save(address);
        redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Added address successfully"));
        return "redirect:/logout";
    }

    @GetMapping("/set/{id}")
    public String setAddress(@PathVariable("id") long id, HttpSession session, @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes redirectAttributes)
    {
        Optional<Order> optionalOrder = cartService.getCartOrderOptional(userDetails.getUsername());
        if(optionalOrder.isPresent())
        {
            var address = optionalOrder.get().address;
            String zipcode = address.getZipcode();
            if(!addressRepo.findById(id).get().getZipcode().equals(zipcode))
            {
                redirectAttributes.addFlashAttribute("flashWarning", Collections.singletonList("Your cart has products from sellers out of this zipcode, possible address is set automativally"));
                session.setAttribute("addressId", address.getId());
                return "redirect:/";
            }
        }
        session.setAttribute("addressId", id);
        logger.info("--------------------------------Address Session set------------------------------------------------------------");
        logger.info(String.valueOf(id));
        logger.info("--------------------------------Address Session set------------------------------------------------------------");
        return "redirect:/";
    }

}

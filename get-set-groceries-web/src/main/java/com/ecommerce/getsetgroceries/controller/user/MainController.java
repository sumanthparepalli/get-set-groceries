package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.UserAddress;
import com.ecommerce.getsetgroceries.repositories.OrderRepo;
import com.ecommerce.getsetgroceries.repositories.UserAddressRepo;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@SessionAttributes("addressId")
public class MainController {

    private final UserService userService;
    private final OrderRepo orderRepo;
    private final UserAddressRepo addressRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());;

    public MainController(UserService userService, OrderRepo orderRepo, UserAddressRepo addressRepo) {
        this.userService = userService;
        this.orderRepo = orderRepo;
        this.addressRepo = addressRepo;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @ModelAttribute("addressId")
    public long getAddressId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        var cartOrder = orderRepo.getOrderByPaidFalseAndUsername(userDetails.getUsername());
        if (cartOrder.isPresent()) {
            logger.info("--------------------------------Address Session set CartOrder---------------------------------------------");
            logger.info(String.valueOf(cartOrder.get().address.getId()));
            logger.info("--------------------------------Address Session set------------------------------------------------------------");
            return cartOrder.get().address.getId();
        } else {
            Optional<UserAddress> firstAddress = addressRepo.findFirstByUser(userDetails.getUser());
            long id = firstAddress.isEmpty() ? 0L : firstAddress.get().getId();
            logger.info("--------------------------------Address Session set first Address---------------------------------------------");
            logger.info(String.valueOf(id));
            logger.info("--------------------------------Address Session set------------------------------------------------------------");
            return id;
        }
    }

    @GetMapping("/user")
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public String userHome() {
        return "Hello User";
    }
//
//    @GetMapping("/seller")
//    @ResponseBody
//    public String adminHome() {
//        return "Hello admin";
//    }

}

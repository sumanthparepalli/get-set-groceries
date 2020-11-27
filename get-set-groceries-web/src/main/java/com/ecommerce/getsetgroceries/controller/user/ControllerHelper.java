package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.repositories.OrderRepo;
import com.ecommerce.getsetgroceries.repositories.UserAddressRepo;
import com.ecommerce.getsetgroceries.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ControllerHelper {

    private final UserService userService;
    private final OrderRepo orderRepo;
    private final UserAddressRepo addressRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ControllerHelper(UserService userService, OrderRepo orderRepo, UserAddressRepo addressRepo) {
        this.userService = userService;
        this.orderRepo = orderRepo;
        this.addressRepo = addressRepo;
    }
}

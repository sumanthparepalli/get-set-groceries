package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.CartReq;
import com.ecommerce.getsetgroceries.models.Order;
import com.ecommerce.getsetgroceries.models.OrderDetails;
import com.ecommerce.getsetgroceries.repositories.*;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.CartService;
import com.ecommerce.getsetgroceries.services.OrderDetailsService;
import com.ecommerce.getsetgroceries.services.OrderService;
import com.ecommerce.getsetgroceries.viewmodels.CartModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;
    private final UserAddressRepo addressRepo;
    private final ProductRepo productRepo;
    private final OrderDetailsRepo orderDetailsRepo;
    private final ObjectMapper mapper;
    private final CartService cartService;
    private final OrderDetailsService orderDetailsService;
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;


    public CartController(OrderRepo orderRepo, UserRepo userRepo, SellerRepo sellerRepo, UserAddressRepo addressRepo, ProductRepo productRepo, OrderDetailsRepo orderDetailsRepo, ObjectMapper mapper, CartService cartService, OrderDetailsService orderDetailsService, OrderService orderService) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.sellerRepo = sellerRepo;
        this.addressRepo = addressRepo;
        this.productRepo = productRepo;
        this.orderDetailsRepo = orderDetailsRepo;
        this.mapper = mapper;
        this.cartService = cartService;
        this.orderDetailsService = orderDetailsService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public String cartHomeRedirect() {
        return "redirect:/cart/";
    }

    @GetMapping("/")
    public String showCart(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int updateCount = cartService.validateCart(userDetails.getUsername());
        if(updateCount>0)
        {
            model.addAttribute("flashWarning",Collections.singletonList(updateCount + " products are removed " +
                    "automatically from cart as they are no longer available or " +
                    "the required quantity isn't available :("));
            cartService.validateCartTotal(userDetails.getUsername());
        }
//        Double promotion = cartService.calculatePromotion(userDetails.getUsername());
        CartModel cartModel = cartService.getCartModel(userDetails.getUsername());
        model.addAttribute("cartModel", cartModel);
        return "cart";
    }


    @PostMapping({"", "/"})
    public String updateCart(@ModelAttribute("cartModel") CartModel cartModel, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        logger.info(cartModel.toString());
        ArrayList<String> flashErrors = new ArrayList<>();
        for (OrderDetails x : cartModel.getOrderDetails()) {
            if (!orderDetailsService.updateQuantity(x.getId(), x.getQuantity())) {
                flashErrors.add("unable to update few items' quantity to desired value. However, max possible value is set");
            }
        }
        Order order = cartModel.getOrder();
        orderService.updateOrderTotal(order.getId(), orderDetailsRepo.findOrderTotalByOrderId(order.getId()));
        cartService.calculatePromotion(userDetails.getUsername());
        if (flashErrors.size() > 0)
            redirectAttributes.addFlashAttribute("flashError", flashErrors);
        return "redirect:/cart/";
    }


    /**
     * @param session     to retrieve from session
     * @param cartReq     request body
     * @param userDetails authentication principal
     * @return json response
     */
    @PostMapping(value = "/add")
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public ObjectNode addToCart(HttpSession session, @RequestBody CartReq cartReq, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Object obj = session.getAttribute("addressId");
        long addressId = 0L;
        if (obj != null) {
            addressId = (Long) obj;
        }
        return cartService.addToCart(addressId, cartReq, userDetails);
    }

//    @PostMapping(value = "/remove/{id}")
//    @ResponseBody
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ObjectNode removeItem2(@PathVariable("id") long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        ObjectNode objectNode = mapper.createObjectNode();
//        String username = cartService.getUsernameByOrderDetailsId(id);
//        if (username == null || !username.equals(userDetails.getUsername())) {
//            objectNode.put("status", "400");
//            objectNode.put("message", "Bad Request");
//            return objectNode;
//        }
//        try {
//            orderDetailsService.remove(id);
//            Order order = cartService.getCartOrderOptional(userDetails.getUsername()).get();
//            orderService.updateOrderTotal(order.getId(), orderDetailsRepo.findOrderTotalByOrderId(order.getId()));
//
//            objectNode.put("status", "202");
//            objectNode.put("message", "Item removed from cart!!");
//            objectNode.put("total", order.getAmount());
//        } catch (Exception e) {
//            objectNode.put("status", "500");
//            objectNode.put("message", "Error occurred!!");
//        }
//        return objectNode;
//    }

    @GetMapping(value = "/remove/{id}")
//    @Transactional
    public String removeItem(@PathVariable("id") long id, @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes redirectAttributes) {
        String username = cartService.getUsernameByOrderDetailsId(id);
        if (username == null || !username.equals(userDetails.getUsername())) {
            redirectAttributes.addFlashAttribute("flashError", Collections.singletonList("Bad request"));
            return "redirect:/cart/";
        }
        orderDetailsService.remove(id);
        cartService.validateCartTotal(userDetails.getUsername());
        return "redirect:/cart/";
    }
}

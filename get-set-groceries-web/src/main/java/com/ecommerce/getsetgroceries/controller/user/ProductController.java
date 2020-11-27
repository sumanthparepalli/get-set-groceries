package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.repositories.OrderRepo;
import com.ecommerce.getsetgroceries.repositories.UserAddressRepo;
import com.ecommerce.getsetgroceries.services.ProductService;
import com.ecommerce.getsetgroceries.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderRepo orderRepo;
    private final UserAddressRepo addressRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public ProductController(ProductService productService, UserService userService, OrderRepo orderRepo, UserAddressRepo addressRepo) {
        this.productService = productService;
        this.userService = userService;
        this.orderRepo = orderRepo;
        this.addressRepo = addressRepo;
    }

    @GetMapping({"", "/", "/all"})
    public String displayProductsRedirect() {
        return "redirect:/products/all/0";
    }

    @GetMapping(value = {"/all/{pgNo}"})
    public String displayProducts(HttpSession session, Model model, @PathVariable("pgNo") int pgNo, RedirectAttributes redirectAttributes) {
        Object obj = session.getAttribute("addressId");
        long addressId=0L;
        if(obj!=null)
        {
            addressId = (Long)obj;
        }
        if (addressId == 0) {
            redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Address should be set first"));
            return "redirect:/";
        }
        var products = productService.getProductDetailsByAddressId(addressId, pgNo);
        model.addAttribute("products", products);
        model.addAttribute("pgNo", pgNo);
        model.addAttribute("pgNo_next", pgNo + 1);
        model.addAttribute("pgNo_prev", pgNo - 1);
        return "products";
    }

    @GetMapping("/{id}")
    public String displayProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("sellers", productService.getProductSellersByProductId(id));
        return "product-details";
    }
}

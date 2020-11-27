package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.Product;
import com.ecommerce.getsetgroceries.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping({"", "/"})
    public String search(@RequestParam(value = "query", defaultValue = "") String key, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
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
//        if(key.length()<3)
//        {
//
//            return "redirect:/";
//        }
        Set<Product> products = searchService.search(key, addressId);
        model.addAttribute("products", products)
                .addAttribute("pages", false);
        return "products";
    }

}

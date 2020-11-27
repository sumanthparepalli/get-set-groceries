package com.ecommerce.getsetgroceries.controller.seller;

import com.ecommerce.getsetgroceries.models.CreditSchemeContri;
import com.ecommerce.getsetgroceries.models.CreditSchemeReq;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.seller.SellerCreditService;
import com.ecommerce.getsetgroceries.validations.NewCreditSchemeReqValidation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/seller/credit")
public class SellerCreditController {

    private final SellerCreditService sellerCreditService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new NewCreditSchemeReqValidation());
    }

    public SellerCreditController(SellerCreditService sellerCreditService) {
        this.sellerCreditService = sellerCreditService;
    }

    @GetMapping
    public String getAllRequests(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CreditSchemeReq> allCreditRequests = sellerCreditService.getAllCreditRequests(userDetails.getUser().seller.getId());
        model.addAttribute("credits", allCreditRequests);
        List<Double> amounts = new ArrayList<>();
        allCreditRequests.forEach(x -> {
            amounts.add(x.contris.stream().mapToDouble(CreditSchemeContri::getAmount).sum());
        });
        model.addAttribute("amounts", amounts);
        return "seller/credit-requests";
    }

    @GetMapping("/new")
    public String newCreditRequestView(Model model) {
        model.addAttribute("newCredit", new CreditSchemeReq());
        return "seller/newCreditRequest";
    }

    @PostMapping("/new")
    public String newCreditRequest(@ModelAttribute("newCredit") @Valid CreditSchemeReq creditSchemeReq, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors())
        {
            return "seller/newCreditRequest";
        }
        sellerCreditService.addNewRequest(creditSchemeReq,userDetails.getUser().seller.getId());
        return "redirect:/seller/credit";
    }
}

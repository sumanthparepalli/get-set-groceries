package com.ecommerce.getsetgroceries.controller.user;

import com.ecommerce.getsetgroceries.models.CreditSchemeContri;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.CreditService;
import com.ecommerce.getsetgroceries.services.PaymentService;
import com.ecommerce.getsetgroceries.viewmodels.CreditRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SignatureException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/credit")
public class CreditController {

    private final CreditService creditService;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public CreditController(CreditService creditService, PaymentService paymentService, ObjectMapper objectMapper) {
        this.creditService = creditService;
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String getAllCreditSchemes(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        model.addAttribute("credits", creditService.getAllCreditSchemes(userDetails.getUser().getId()));
        return "credit_scheme";
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public ObjectNode creditSeller(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CreditRequestBody creditRequestBody) throws JSONException, SignatureException, IOException {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            paymentService.CompleteCreditPayment(creditRequestBody.getCallid(), userDetails.getUser(), creditRequestBody.getId(), creditRequestBody.getAmount());
            objectNode.put("status", 200);
            objectNode.put("message", "Payment successful");
        } catch (SignatureException e) {
            objectNode.put("status", 500);
            objectNode.put("message", "Error in creating payment token");
        } catch (IOException e) {
            objectNode.put("status", 500);
            objectNode.put("message", "Error in retrieving payment details");
        } catch (Exception e) {
            e.printStackTrace();
            objectNode.put("status", 400);
            objectNode.put("message", "Payment failed");
        }
        return objectNode;
    }

    @GetMapping("/all")
    public String getAllCredits(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User user = userDetails.getUser();
        List<CreditSchemeContri> allMyCreditSchemes = creditService.getAllMyCreditSchemes(user);
        model.addAttribute("credits",allMyCreditSchemes);
        List<LocalDate> releaseDate = new ArrayList<>();
        allMyCreditSchemes.forEach(x -> releaseDate.add(x.getDate().plusDays(x.creditSchemeReq.getLockingPeriod())));
        model.addAttribute("release", releaseDate);
        return "myCredits";
    }

}

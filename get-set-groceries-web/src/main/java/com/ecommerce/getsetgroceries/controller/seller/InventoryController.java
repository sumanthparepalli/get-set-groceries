package com.ecommerce.getsetgroceries.controller.seller;

import com.ecommerce.getsetgroceries.models.Inventory;
import com.ecommerce.getsetgroceries.models.Seller;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.resultMappings.ProductInventorySeller;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.services.CategoryService;
import com.ecommerce.getsetgroceries.services.seller.SellerInventoryService;
import com.ecommerce.getsetgroceries.services.seller.SellerProductService;
import com.ecommerce.getsetgroceries.validations.NewProductValidation;
import com.ecommerce.getsetgroceries.viewmodels.NewProduct;
import com.ecommerce.getsetgroceries.viewmodels.PartialProduct;
import com.ecommerce.getsetgroceries.viewmodels.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/seller/inventory")
public class InventoryController {

    private final SellerInventoryService sellerInventoryService;
    private final CategoryService categoryService;
    private final NewProductValidation newProductValidation;
    private final SellerProductService sellerProductService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public InventoryController(SellerInventoryService sellerInventoryService, CategoryService categoryService, NewProductValidation newProductValidation, SellerProductService sellerProductService) {
        this.sellerInventoryService = sellerInventoryService;
        this.categoryService = categoryService;
        this.newProductValidation = newProductValidation;
        this.sellerProductService = sellerProductService;
    }

    @ModelAttribute("categories")
    public List<String> getCategories() {
        return categoryService.getAllCategoriesNames();
    }

    @ModelAttribute("products")
    public List<PartialProduct> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User user = userDetails.getUser();
        return sellerProductService.getPartialProducts(user.seller.getId());
    }

    @ModelAttribute("existingProducts")
    public List<PartialProduct> getNonSellingProducts(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User user = userDetails.getUser();
        return sellerProductService.getPartialProductsExisting(user.seller.getId());
    }

    @InitBinder({"product", "updateProduct"})
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(newProductValidation);
    }

    @GetMapping({"", "/"})
    public String redirectInventory() {
        return "redirect:/seller/inventory/all";
    }

    @GetMapping("/all")
    public String inventory(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Seller seller = user.seller;
        List<ProductInventorySeller> sellerInventory = sellerInventoryService.getSellerInventory(seller.getId());
        model.addAttribute("items", sellerInventory);
        model.addAttribute("seller", seller);
        return "seller/inventory";
    }

    @GetMapping("/add")
    public String addToInventoryView(Model model) {
        model.addAttribute("product", new NewProduct());
//        model.addAttribute("categories",categoryService.getAllCategoriesNames());
        return "seller/addNewProduct";
    }

    @PostMapping("/add")
    public String addToInventory(@ModelAttribute("product") @Valid NewProduct product, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.toString());
            return "seller/addNewProduct";
        }
        sellerProductService.addNewProduct(product, userDetails.getUser(), false);
        redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Successfully added to your inventory"));
        return "redirect:/seller/inventory/all";
    }

    @GetMapping("/update")
    public String updateInventoryView(Model model) {
        model.addAttribute("product", new NewProduct());
        return "seller/updateInventory";
    }

    @PostMapping("/update")
    public String updateInventory(@ModelAttribute("product") @Valid NewProduct product, BindingResult bindingResult,@AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes redirectAttributes)
    {
        if(bindingResult.hasErrors())
        {
            return "seller/updateInventory";
        }
        sellerProductService.addNewProduct(product, userDetails.getUser(), true);
        redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Successfully updated"));
        return "redirect:/seller/inventory/all";
    }

    @GetMapping("/add/existing")
    public String addExistingInventoryView(Model model)
    {
        model.addAttribute("inventory", new Inventory());
        return "seller/addExistingToInventory";
    }

    @PostMapping("/add/existing")
    public String addExistingInventory(@ModelAttribute("inventory") @Valid Inventory inventory, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if (bindingResult.hasErrors())
        {
            return "seller/addExistingToInventory";
        }
        sellerProductService.addExistingProduct(inventory, userDetails.getUser());
        redirectAttributes.addFlashAttribute("flashSuccess", Collections.singletonList("Successfully added to your inventory"));
        return "redirect:/seller/inventory/all";
    }


    @GetMapping("/get/{id}")
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public ResponseEntity<NewProduct> getInventoryItem(@PathVariable("id") long id, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        NewProduct inventory = sellerInventoryService.getInventory(userDetails.getUser().seller.getId(),id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/get/product/{id}")
    @CrossOrigin(origins = "http://localhost:8082")
    @ResponseBody
    public ResponseEntity<ProductResponse> getInventoryItem(@PathVariable("id") long id)
    {
        return ResponseEntity.ok(sellerInventoryService.getProductResponse(id));
    }

}

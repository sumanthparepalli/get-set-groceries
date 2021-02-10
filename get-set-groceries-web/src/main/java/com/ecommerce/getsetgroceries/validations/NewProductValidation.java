package com.ecommerce.getsetgroceries.validations;

import com.ecommerce.getsetgroceries.viewmodels.NewProduct;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class NewProductValidation implements Validator {
    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return NewProduct.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewProduct product = (NewProduct) o;
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"product_id","required.product_id");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"product_name","required.product_name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"product_description","required.product_description");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"quantity","required.quantity");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"discount","required.discount");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"price","required.price");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"categories","required.categories");
        Object img = errors.getFieldValue("images");
        if(img==null || ((MultipartFile[])img).length==0)
        {
            errors.rejectValue("images","required.images");
        }
        if(product.getPrice() !=null && product.getPrice() <=0)
        {
            errors.rejectValue("price","non-negative.price");
        }
        if(product.getQuantity() !=null && product.getQuantity() <=0)
        {
            errors.rejectValue("quantity","non-negative.quantity");
        }
        if(product.getDiscount() !=null && (product.getDiscount() <0 || product.getDiscount() >100))
        {
            errors.rejectValue("discount","range.discount");
        }
    }
}

package com.ecommerce.getsetgroceries.validations;

import com.ecommerce.getsetgroceries.models.CreditSchemeReq;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NewCreditSchemeReqValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CreditSchemeReq.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreditSchemeReq creditSchemeReq = (CreditSchemeReq) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amountRequested", "required.amount");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "discount", "required.discount");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lockingPeriod", "required.lockingPeriod");
        if(creditSchemeReq.getAmountRequested() != null && creditSchemeReq.getAmountRequested()<=0)
        {
            errors.rejectValue("amount","non-negative.amount");
        }
        if(creditSchemeReq.getDiscount() != null && (creditSchemeReq.getDiscount()<=0 || creditSchemeReq.getDiscount()>=100))
        {
            errors.rejectValue("discount","range.discount");
        }
        if(creditSchemeReq.getLockingPeriod() != null && creditSchemeReq.getLockingPeriod()<=0)
        {
            errors.rejectValue("lockingPeriod","non-negative.lockingPeriod");
        }
    }
}

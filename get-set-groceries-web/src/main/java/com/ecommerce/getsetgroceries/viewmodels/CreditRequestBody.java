package com.ecommerce.getsetgroceries.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CreditRequestBody {
    public long id;
    public double amount;
    public String callid;
}

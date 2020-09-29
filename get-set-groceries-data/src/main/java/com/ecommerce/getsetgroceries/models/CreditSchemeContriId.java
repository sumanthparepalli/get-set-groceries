package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreditSchemeContriId implements Serializable {
    private CreditSchemeReq creditSchemeReq;
    private User user;

    public CreditSchemeContriId(CreditSchemeReq creditSchemeReq, User user) {
        this.creditSchemeReq = creditSchemeReq;
        this.user = user;
    }
}

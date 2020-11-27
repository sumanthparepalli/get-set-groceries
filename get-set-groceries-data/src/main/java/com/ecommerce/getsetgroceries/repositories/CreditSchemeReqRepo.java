package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.CreditSchemeReq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditSchemeReqRepo extends JpaRepository<CreditSchemeReq, Long> {
    List<CreditSchemeReq> findAllBySellerId(long sellerId);
}

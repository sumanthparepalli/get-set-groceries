package com.ecommerce.getsetgroceries.services.seller;

import com.ecommerce.getsetgroceries.models.CreditSchemeReq;
import com.ecommerce.getsetgroceries.repositories.CreditSchemeReqRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SellerCreditService {

    private final CreditSchemeReqRepo creditSchemeReqRepo;

    public SellerCreditService(CreditSchemeReqRepo creditSchemeReqRepo) {
        this.creditSchemeReqRepo = creditSchemeReqRepo;
    }

    public List<CreditSchemeReq> getAllCreditRequests(long id)
    {
        return creditSchemeReqRepo.findAllBySellerId(id);
    }

    @Transactional
    public void addNewRequest(CreditSchemeReq creditSchemeReq, long id) {
        creditSchemeReq.setAmountReceived(0d);
        creditSchemeReq.setAmountCleared(0d);
        creditSchemeReq.setSellerId(id);
        creditSchemeReqRepo.save(creditSchemeReq);
    }
}

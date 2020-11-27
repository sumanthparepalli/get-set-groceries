package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.CreditSchemeContri;
import com.ecommerce.getsetgroceries.models.CreditSchemeReq;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.repositories.CreditSchemeContriRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CreditService {

    private final CreditSchemeContriRepo creditSchemeContriRepo;
    @PersistenceContext
    private EntityManager em;

    public CreditService(CreditSchemeContriRepo creditSchemeContriRepo) {
        this.creditSchemeContriRepo = creditSchemeContriRepo;
    }

    public List<CreditSchemeReq> getAllCreditSchemes(long id) {
        List resultList = em.createNativeQuery("SELECT * FROM credit_scheme_req WHERE amount_received<amount_requested and sellers_id IN " +
                "(SELECT s.id FROM sellers s WHERE s.zipcode IN " +
                "(SELECT zipcode FROM user_address ua WHERE ua.users_id = :id))", CreditSchemeReq.class)
                .setParameter("id", id)
                .getResultList();
        List<CreditSchemeReq> res = new ArrayList<>();
        resultList.forEach(x -> res.add((CreditSchemeReq)x));
        return res;
    }

    public List<CreditSchemeContri> getAllMyCreditSchemes(User user) {
        return creditSchemeContriRepo.getAllByUserIdOrderByDateDesc(user.getId());
    }
}

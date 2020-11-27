package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.CreditSchemeContri;
import com.ecommerce.getsetgroceries.models.CreditSchemeContriId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditSchemeContriRepo extends JpaRepository<CreditSchemeContri, CreditSchemeContriId> {

    @Query("select c from CreditSchemeContri c where c.userId=:userId order by c.date desc , (c.amount-c.amountResolved) desc ")
    List<CreditSchemeContri> getAllByUserIdOrderByDateDesc(long userId);
}

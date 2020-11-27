package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<OrderPayment, String> {
}

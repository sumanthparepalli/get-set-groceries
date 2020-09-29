package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order,Long> {
}

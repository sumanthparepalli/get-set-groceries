package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.user.username=:username and o.paid=false ")
    public Optional<Order> getOrderByPaidFalseAndUsername(String username);
}

package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller, Long> {
}

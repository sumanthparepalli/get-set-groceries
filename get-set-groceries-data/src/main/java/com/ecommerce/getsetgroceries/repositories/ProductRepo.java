package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products WHERE NAME LIKE concat(:key,'%') UNION SELECT * FROM products WHERE NAME LIKE concat('%_':key,'%')", nativeQuery = true)
    public List<Product> getAllByNameLikeCustomOrder(String key);
}

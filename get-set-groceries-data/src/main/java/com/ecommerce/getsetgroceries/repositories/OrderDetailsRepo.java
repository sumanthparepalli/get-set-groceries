package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Long> {

    @Query(value = "select sum(price) from order_details where order_id = :orderId", nativeQuery = true)
    public Double findOrderTotalByOrderId(long orderId);

    @Query(value = "select * from order_details where order_id=:orderId " +
            "and seller_id=:sellerId and product_id=:productId", nativeQuery = true)
    public Optional<OrderDetails> findByKeys(long orderId, long sellerId, long productId);

    public List<OrderDetails> findAllBySellerIdOrderByDeliveredAscIdDesc(long sellerId);

}

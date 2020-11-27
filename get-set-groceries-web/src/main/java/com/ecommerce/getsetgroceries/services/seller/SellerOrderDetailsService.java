package com.ecommerce.getsetgroceries.services.seller;

import com.ecommerce.getsetgroceries.models.OrderDetails;
import com.ecommerce.getsetgroceries.repositories.OrderDetailsRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class SellerOrderDetailsService {

    private final OrderDetailsRepo orderDetailsrepo;

    @PersistenceContext
    private EntityManager em;

    public SellerOrderDetailsService(OrderDetailsRepo orderDetailsrepo) {
        this.orderDetailsrepo = orderDetailsrepo;
    }

    public List<OrderDetails> getOrderDetails(long sellerId)
    {
        return orderDetailsrepo.findAllBySellerIdOrderByDeliveredAscIdDesc(sellerId);
    }

    public void markDelivered(String key, long id) {
        String[] split = key.split("-");
        long orderId = Long.parseLong(split[0]);
        long productId = Long.parseLong(split[1]);
        em.createQuery("UPDATE OrderDetails set delivered = true where sellerId=:sellerId " +
                "and orderId = :orderId and productId=:productId")
                .setParameter("sellerId", id)
                .setParameter("productId", productId)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
}

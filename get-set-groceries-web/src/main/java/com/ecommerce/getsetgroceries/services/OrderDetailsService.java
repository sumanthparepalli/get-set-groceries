package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.repositories.OrderDetailsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class OrderDetailsService {

    private final OrderDetailsRepo orderDetailsRepo;
    @PersistenceContext
    private EntityManager em;
    Object target;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    public OrderDetailsService(OrderDetailsRepo orderDetailsRepo) {
        this.orderDetailsRepo = orderDetailsRepo;
    }

    public boolean updateQuantity(long id, long quantity)
    {

        int updateCount = em.createQuery("update OrderDetails o set o.quantity=:quantity, o.price=o.pricePerUnit*:quantity where o.id=:id " +
                "and :quantity <= (select i.quantity from Inventory i where i.productId=o.productId and i.sellerId=o.sellerId)")
                .setParameter("id", id)
                .setParameter("quantity", quantity)
                .executeUpdate();
        logger.info(String.valueOf(updateCount));
        if(updateCount==0)
        {
            em.createQuery("update OrderDetails o set o.quantity=(select i.quantity from Inventory i where i.productId=o.productId and i.sellerId=o.sellerId) where o.id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            em.createQuery("update OrderDetails o set o.price=o.pricePerUnit*o.quantity where o.id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
        }
        return updateCount==1;
    }

    public void remove(long id) {
        orderDetailsRepo.deleteById(id);
    }
}

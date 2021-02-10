package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.Order;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.repositories.OrderRepo;
import com.ecommerce.getsetgroceries.repositories.UserRepo;
import com.ecommerce.getsetgroceries.serviceProxy.ImageServiceProxy;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;

    @PersistenceContext
    private EntityManager em;
    private final UserRepo userRepo;
    private final ImageServiceProxy imageServiceProxy;

    public OrderService(OrderRepo orderRepo, UserRepo userRepo, ImageServiceProxy imageServiceProxy) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.imageServiceProxy = imageServiceProxy;
    }

    public void updateOrderTotal(long orderId, double total) {
        em.createQuery("update Order set amount=:total where id=:orderId")
                .setParameter("orderId", orderId)
                .setParameter("total", total)
                .executeUpdate();

    }

    public void remove(long id) {
        orderRepo.deleteById(id);
    }

    public List<Order> allOrders(String username) {
        User user = userRepo.getUserByUsername(username);
        Hibernate.initialize(user.orders);
        user.orders.forEach(Hibernate::initialize);
        Collections.reverse(user.orders);
        return user.orders;
    }

    public List<List<String>> getCartProductImages(Collection<Order> orders) {
        return orders.stream().map(x -> x.orderDetails.stream().map(y -> imageServiceProxy.retrieveImageAsBase64(y.product.imageId.get(0))).collect(Collectors.toList())).collect(Collectors.toList());
    }

}

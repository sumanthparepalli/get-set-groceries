package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.*;
import com.ecommerce.getsetgroceries.repositories.*;
import com.ecommerce.getsetgroceries.security.services.UserDetailsImpl;
import com.ecommerce.getsetgroceries.viewmodels.CartModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class CartService {
    private final ObjectMapper mapper;
    private final OrderDetailsRepo orderDetailsRepo;
    private final OrderService orderService;
    private final SellerRepo sellerRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final UserAddressRepo addressRepo;
    @PersistenceContext
    private EntityManager em;

    public CartService(ObjectMapper mapper, OrderDetailsRepo orderDetailsRepo, OrderService orderService, SellerRepo sellerRepo, ProductRepo productRepo, OrderRepo orderRepo, UserAddressRepo addressRepo) {
        this.mapper = mapper;
        this.orderDetailsRepo = orderDetailsRepo;
        this.orderService = orderService;
        this.sellerRepo = sellerRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.addressRepo = addressRepo;
    }

    public int validateCart(String username) {
        Optional<Order> cartOrderOptional = getCartOrderOptional(username);
        AtomicInteger updateCount = new AtomicInteger();
        cartOrderOptional.ifPresent(cart -> {
            updateCount.set(em.createNativeQuery("delete from order_details od where order_id=:orderId and  od.quantity> (select quantity from inventory i where i.seller_id=od.seller_id and i.product_id=od.product_id)")
                    .setParameter("orderId", cart.getId())
                    .executeUpdate());
        });
        return updateCount.get();
    }

    public List<OrderDetails> getCartOrderDetails(String username) {
        Optional<Order> optionalOrder = getCartOrderOptional(username);
        return optionalOrder.map(order -> order.orderDetails).orElse(null);
    }

    public Optional<Order> getCartOrderOptional(String username) {
        return orderRepo.getOrderByPaidFalseAndUsername(username);
    }

    public String getUsernameByOrderDetailsId(long id) {
        Query query = em.createQuery("select u.username from OrderDetails od JOIN Order o on od.orderId=o.id JOIN User u on o.userId=u.id where od.id=:id")
                .setParameter("id", id);
        try {
            return (String) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CartModel getCartModel(String username) {
        Optional<Order> optionalOrder = orderRepo.getOrderByPaidFalseAndUsername(username);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Hibernate.initialize(order.orderDetails);
            return new CartModel(order, order.orderDetails);
        } else {
            return null;
        }
    }
    public ObjectNode addToCart(long addressId, CartReq cartReq, UserDetailsImpl userDetails) {

        ObjectNode result;
        if (addressId == 0L) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("status", 404);
            objectNode.put("message", "Address to be set first");
            result = objectNode;
        } else {
            Seller seller = sellerRepo.findById(cartReq.getSellerId()).get();
            User user = userDetails.getUser();
            Product product = productRepo.findById(cartReq.getProductId()).get();
            Optional<Order> optionalOrder = orderRepo.getOrderByPaidFalseAndUsername(user.getUsername());
            UserAddress address = addressRepo.findById(addressId).get();
            Order order;
            if (optionalOrder.isEmpty()) {
                order = new Order(user.getId(), addressId, 0.0, false, false);
                orderRepo.save(order);
            } else {
                order = optionalOrder.get();
            }
            double price_per_unit = (double) em.createQuery("select i.price from Inventory i where i.seller.id = :seller_id and i.product.id=:product_id")
                    .setParameter("seller_id", seller.getId())
                    .setParameter("product_id", product.getId())
                    .getSingleResult();
            OrderDetails orderDetails;
            Optional<OrderDetails> optionalOrderDetails = orderDetailsRepo.findByKeys(order.getId(), product.getId(), seller.getId());
            double amt = 0;
            if (optionalOrderDetails.isPresent()) {
                orderDetails = optionalOrderDetails.get();
                amt -= orderDetails.getPrice();
                orderDetails = (OrderDetails) em.createNamedQuery("find_by_id").setParameter("seller_id", seller.getId())
                        .setParameter("product_id", product.getId())
                        .setParameter("order_id", order.getId())
                        .getSingleResult();
                orderDetails.setQuantity(orderDetails.getQuantity() + cartReq.getQuantity());
                orderDetails.setPrice(orderDetails.getPricePerUnit() * orderDetails.getQuantity());
            } else {
                orderDetails = new OrderDetails(cartReq.getQuantity(), price_per_unit, price_per_unit * cartReq.getQuantity(), false, order.getId(), product.getId(), seller.getId());
                orderDetailsRepo.save(orderDetails);
            }
            amt += orderDetails.getPrice();
            order.setAmount(order.getAmount() + amt);
            calculatePromotion(user.getUsername());
            orderRepo.save(order);
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("status", 200);
            objectNode.put("message", "Added to cart successfully");
            result = objectNode;
        }
        return result;
    }

    public double validateCartTotal(String username) {
        Optional<Order> optionalOrder = getCartOrderOptional(username);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Double orderTotal = orderDetailsRepo.findOrderTotalByOrderId(order.getId());
            if (orderTotal == null) {
                orderService.remove(order.getId());
            } else {
                orderService.updateOrderTotal(order.getId(), orderTotal);
                return orderTotal;
            }
        }
        return -1.0d;
    }

    public Double calculatePromotion(String username)
    {
        Optional<Order> cartOrderOptional = getCartOrderOptional(username);
        AtomicReference<Double> promotionAmount = new AtomicReference<>((double) 0);
        DecimalFormat df = new DecimalFormat("0.00");
        cartOrderOptional.ifPresent(cart -> {
            List<Tuple> resultList = em.createQuery("select  o.sellerId,sum(o.price) as amount from OrderDetails o where o.orderId=:orderId group by o.sellerId", Tuple.class)
                    .setParameter("orderId", cart.getId())
                    .getResultList();
            for(var i:resultList)
            {
                List<Tuple> tuple = (List<Tuple>) em.createNativeQuery("select cc.amount,csr.discount from credit_scheme_contri cc join credit_scheme_req csr " +
                        "on cc.credit_scheme_id = csr.id where csr.sellers_id=:sellerId and cc.amount > cc.amount_resolved " +
                        " and DATE_ADD(cc.date,INTERVAL csr.locking_period DAY)<=DATE(NOW()) ORDER BY cc.date DESC LIMIT 1", Tuple.class)
                        .setParameter("sellerId", i.get(0, Long.class))
                        .getResultList();
                if(tuple!=null && tuple.size()>0)
                {
                    Double amount = i.get("amount", Double.class);
                    Float discount = tuple.get(0).get("discount",Float.class);
                    Float balance = tuple.get(0).get("amount",Float.class)*(1+discount/100);
                    if(amount>balance)
                    {
                        promotionAmount.updateAndGet(x -> Double.valueOf(df.format(balance+x)));
                    }
                    else
                    {
                        promotionAmount.updateAndGet(x -> Double.valueOf(df.format(amount+x)));
                    }
                }
            }
            cart.setPromotion(promotionAmount.get());
            cart.setTotal(Double.parseDouble(df.format(cart.getAmount()-promotionAmount.get())));
        });
        return promotionAmount.get();
    }
}

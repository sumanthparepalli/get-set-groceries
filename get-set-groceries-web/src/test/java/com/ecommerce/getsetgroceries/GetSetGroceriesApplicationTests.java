package com.ecommerce.getsetgroceries;

import com.ecommerce.getsetgroceries.checkout.xPay;
import com.ecommerce.getsetgroceries.models.*;
import com.ecommerce.getsetgroceries.repositories.*;
import com.ecommerce.getsetgroceries.resultMappings.ProductInventorySeller;
import com.ecommerce.getsetgroceries.services.CartService;
import com.ecommerce.getsetgroceries.services.OrderService;
import com.ecommerce.getsetgroceries.services.ProductService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hibernate.Hibernate;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GetSetGroceriesApplicationTests {

    @Autowired
    InventoryRepo inventoryRepo;
    @Autowired
    EntityManager em;
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserAddressRepo userAddressRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CreditSchemeContriRepo creditSchemeContriRepo;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CartService cartService;

    @Test
    void contextLoads() {
    }

    @Test
    void LoadProducts() {
//        logger.info(productService.getProductsDetailsByZipcode("01798",0).toString());
        Query nativeQuery = em.createNativeQuery("select s.id as seller_id," +
                " s.seller_name," +
                " i.price," +
                " i.quantity," +
                " i.discount," +
                " i.demand " +
                "from products p " +
                "join inventory i on p.id = i.product_id " +
                "join sellers s on i.seller_id = s.id " +
                "where p.id=:productId", "product-inventory-seller");
        nativeQuery.setParameter("productId", 1);
        List<ProductInventorySeller> productList = new ArrayList<>();
        //noinspection unchecked
        nativeQuery.getResultList().iterator().forEachRemaining((obj) -> productList.add((ProductInventorySeller) obj));
        for (var i : productList) {
            logger.info(i.toString());
        }
    }

    @Test
    @DirtiesContext
    void cartTest() {
//        var x = userAddressRepo.findFirstByUser(userRepo.getUserByUsername("Dana935"));
//        logger.info(x.isEmpty()? "null" :x.get().toString());
//        Double o = (Double) em.createQuery("select i.price from Inventory i where i.seller.id = :seller_id and i.product.id=:product_id")
//                .setParameter("seller_id", 1L)
//                .setParameter("product_id", 1L)
//                .getSingleResult();
//        logger.info(String.valueOf(o));
//        logger.info(orderRepo.getOrderByPaidFalseAndUsername("sumanth").get().toString());
//        logger.info(inventoryRepo.findById(new InventoryID(sellerRepo.findById(1L).get(),productRepo.findById(1L).get())).get().toString());
        Inventory inventory = new Inventory(1, 7, 20, 40, 1, 0);
//        inventory.setProduct(productRepo.findById(2L).get());
//        inventory.setSeller(sellerRepo.findById(1L).get());
        inventoryRepo.save(inventory);
        inventory = inventoryRepo.findById(new InventoryID(1, 7)).get();
        logger.info(inventory.product.toString());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void orderTest() {
        Order order = orderRepo.getOrderByPaidFalseAndUsername("sumanth").get();
        Hibernate.initialize(order.orderDetails);
        order.orderDetails.forEach(obj -> obj.setPrice(50));
        orderRepo.save(order);
        Order order2 = orderRepo.getOrderByPaidFalseAndUsername("sumanth").get();
//        logger.info(order2.toString());
        order2.orderDetails.forEach(x -> logger.info(x.toString()));
    }

    @Test
    public void getUsernameByOrderDetailsId() {
        logger.info(cartService.getUsernameByOrderDetailsId(51));
    }

    @Test
    public void allOrders() {
        logger.info(orderService.allOrders("test").toString());
    }

    @Test
    public void orderDate()
    {
        logger.info(orderRepo.findById(5L).get().getOrderDate()+"");
    }

    @Test
    @DirtiesContext
    public void okHTTPTest() throws IOException, SignatureException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://sandbox.api.visa.com/wallet-services-web/payment/data/1600851408632728302?apikey=3LXNZKOIF92IZSK6ENHZ21cV7A_TdR5y6u08gqnUP2tujkhRE")
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .addHeader("x-pay-token", xPay.generateXpaytoken("payment/data/1600851408632728302","apikey=3LXNZKOIF92IZSK6ENHZ21cV7A_TdR5y6u08gqnUP2tujkhRE","41KyfrlSuiLCCZube/Rf1k3#osxxwk63OegZJ0wQ",""))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
//        JSONObject jsonObject = new JSONObject(response.body().string());
        System.out.println(response.code());
//        Date date = new Date(jsonObject.getLong("creationTimeStamp"));
//        System.out.println(jsonObject.getJSONObject("userData").getString("userName"));
    }

    @Test
    void search() {
        System.out.println(productRepo.getAllByNameLikeCustomOrder("%a%"));
    }

    @Test
    @DirtiesContext
    @Transactional
    void newProduct() {
        User user = userRepo.getUserByUsername("sumanth");
        Product product = Product.builder()
                .name("new152")
                .description("new152 is new")
                .build();
//        em.persist(product);
        productRepo.save(product);
        logger.info(String.valueOf(product.getId()));
    }

    @Test
    @DirtiesContext
    void createSave() {
        Product product = Product
                .builder()
                .id(50)
                .name("Apricot - 12x")
                .description("Fresh and juicy")
                .build();
        productRepo.save(product);
        logger.info(productRepo.findById(50L).get().toString());
    }

    @Test
    @DirtiesContext
    @Transactional
    void sqlGetOrdersSeller()
    {
        logger.info(orderDetailsRepo.findAllBySellerIdOrderByDeliveredAscIdDesc(23L).toString());
    }

    @Test
    @Transactional
    void repoAllMyCredits()
    {
        logger.info(creditSchemeContriRepo.getAllByUserIdOrderByDateDesc(52).toString());
    }

    @Test
    void promotion() {
//        List resultList = em.createQuery("select  o.sellerId,sum(o.price) from OrderDetails o where o.orderId=:orderId group by o.sellerId")
//                .setParameter("orderId", 87l)
//                .getResultList();
        logger.info(String.valueOf(cartService.calculatePromotion("sumanth")));
    }
}

package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.Product;
import com.ecommerce.getsetgroceries.repositories.InventoryRepo;
import com.ecommerce.getsetgroceries.repositories.ProductRepo;
import com.ecommerce.getsetgroceries.repositories.UserAddressRepo;
import com.ecommerce.getsetgroceries.resultMappings.ProductInventorySeller;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
@Service
@Transactional
public class ProductService {
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final UserAddressRepo addressRepo;
    @PersistenceContext
    private EntityManager em;

    public ProductService(ProductRepo productRepo, InventoryRepo inventoryRepo, UserAddressRepo addressRepo) {
        this.productRepo = productRepo;
        this.inventoryRepo = inventoryRepo;
        this.addressRepo = addressRepo;
    }

    public List<ProductInventorySeller> getProductSellersByProductId(long productId) {
        Query nativeQuery = em.createNativeQuery("select p.id as product_id, " +
                "p.name as product_name, " +
                "p.description as product_description," +
                " s.id as seller_id," +
                " s.seller_name," +
                " i.price," +
                " i.quantity," +
                " i.discount," +
                " i.demand " +
                "from products p " +
                "join inventory i on p.id = i.product_id " +
                "join sellers s on i.seller_id = s.id " +
                "where p.id=:productId and i.quantity>0", "product-inventory-seller");
        nativeQuery.setParameter("productId", productId);
        List<ProductInventorySeller> productList = new ArrayList<>();
        nativeQuery.getResultList().iterator().forEachRemaining((obj) -> productList.add((ProductInventorySeller) obj));
        return productList;
    }

    public List<Product> getProductDetailsByZipcode(String zipcode, int pgNo) {
        int firstResult = pgNo * 12 + 1;
        Session session = em.unwrap(Session.class);
        List list = session.createSQLQuery("select {p.*} from products p join inventory i on p.id=i.product_id " +
                "join sellers s on s.id=i.seller_id where s.zipcode=:zipcode and i.quantity>0")
                .setParameter("zipcode", zipcode)
                .addEntity("p", Product.class)
                .setFirstResult(firstResult).setMaxResults(12).list();
        if (list == null)
            return null;
        List<Product> productList = new ArrayList<>();
        list.forEach((obj) -> productList.add((Product) obj));
        return productList;
    }

    public List<Product> getProductDetailsByAddressId(long addressId, int pgNo)
    {
        String zipcode = addressRepo.findById(addressId).get().getZipcode();
        return getProductDetailsByZipcode(zipcode,pgNo);
    }


    /*public List<ProductInventorySeller> getSellerDetailsByProductId(long productId)
    {
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
        nativeQuery.setParameter("productId", productId);
        List<ProductInventorySeller> productList = new ArrayList<>();
        nativeQuery.getResultList().iterator().forEachRemaining((obj) -> productList.add((ProductInventorySeller) obj));
        return productList;
    }*/


    public Product getProduct(Long id) {
        return productRepo.findById(id).get();
    }
}

package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.Product;
import com.ecommerce.getsetgroceries.repositories.ProductRepo;
import com.ecommerce.getsetgroceries.repositories.UserAddressRepo;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {

    private final ProductRepo productRepo;
    private final UserAddressRepo addressRepo;
    @PersistenceContext
    private EntityManager em;

    public SearchService(ProductRepo productRepo, UserAddressRepo addressRepo) {
        this.productRepo = productRepo;
        this.addressRepo = addressRepo;
    }

    public Set<Product> search(String key, long addressId) {
        String zipcode = addressRepo.findById(addressId).get().getZipcode();
        Session session = em.unwrap(Session.class);
        List list = session.createSQLQuery("select {p.*} from products p join inventory i on p.id=i.product_id " +
                "join sellers s on s.id=i.seller_id where s.zipcode=:zipcode and p.name like concat('%',:key,'%')")
                .setParameter("key", key)
                .setParameter("zipcode", zipcode)
                .addEntity("p", Product.class)
                .list();
        if (list == null)
            return null;
        Set<Product> productList = new LinkedHashSet<>();
        list.forEach((obj) -> productList.add((Product) obj));
        return productList;
    }
}

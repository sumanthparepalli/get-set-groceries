package com.ecommerce.getsetgroceries.services.seller;

import com.ecommerce.getsetgroceries.models.Inventory;
import com.ecommerce.getsetgroceries.models.InventoryID;
import com.ecommerce.getsetgroceries.models.Product;
import com.ecommerce.getsetgroceries.repositories.InventoryRepo;
import com.ecommerce.getsetgroceries.repositories.ProductRepo;
import com.ecommerce.getsetgroceries.resultMappings.ProductInventorySeller;
import com.ecommerce.getsetgroceries.viewmodels.NewProduct;
import com.ecommerce.getsetgroceries.viewmodels.ProductResponse;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "SellerInventoryServiceSeller")
public class SellerInventoryService {

    @PersistenceContext
    private EntityManager em;
    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;

    public SellerInventoryService(InventoryRepo inventoryRepo, ProductRepo productRepo) {
        this.inventoryRepo = inventoryRepo;
        this.productRepo = productRepo;
    }

    public List<ProductInventorySeller> getSellerInventory(long sellerId)
    {
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
                "where s.id=:sellerId", "product-inventory-seller");
        nativeQuery.setParameter("sellerId", sellerId);
        List<ProductInventorySeller> productList = new ArrayList<>();
        nativeQuery.getResultList().iterator().forEachRemaining((obj) -> productList.add((ProductInventorySeller) obj));
        return productList;
    }

    public NewProduct getInventory(long sellerId, long productId) {
        Inventory inventory = inventoryRepo.findById(new InventoryID(sellerId, productId)).get();
        Product product = productRepo.findById(productId).get();
        Set<String> category = new HashSet<>();
        product.categories.forEach(x -> category.add(x.getCategoryName()));
        return new NewProduct(productId,product.getName(), product.getDescription(), inventory.getQuantity(),inventory.getDiscount(),inventory.getPrice(),category);
    }

    public ProductResponse getProductResponse(long productId)
    {
        Product product = productRepo.findById(productId).get();
        ProductResponse response = new ProductResponse(productId,product.getName(),product.getDescription(), new HashSet<>());
        product.categories.forEach(x -> response.getCategories().add(x.getCategoryName()));
        return response;
    }
}

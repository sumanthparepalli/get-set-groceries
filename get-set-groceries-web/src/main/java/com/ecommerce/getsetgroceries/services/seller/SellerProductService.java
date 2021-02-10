package com.ecommerce.getsetgroceries.services.seller;

import com.ecommerce.getsetgroceries.models.*;
import com.ecommerce.getsetgroceries.repositories.CategoryRepo;
import com.ecommerce.getsetgroceries.repositories.InventoryRepo;
import com.ecommerce.getsetgroceries.repositories.ProductRepo;
import com.ecommerce.getsetgroceries.repositories.SellerRepo;
import com.ecommerce.getsetgroceries.serviceProxy.ImageServiceProxy;
import com.ecommerce.getsetgroceries.viewmodels.NewProduct;
import com.ecommerce.getsetgroceries.viewmodels.PartialProduct;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SellerProductService {
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final SellerRepo sellerRepo;
    private final CategoryRepo categoryRepo;
    private final ImageServiceProxy imageServiceProxy;
    @PersistenceContext
    private EntityManager em;

    public SellerProductService(ProductRepo productRepo, InventoryRepo inventoryRepo, SellerRepo sellerRepo, CategoryRepo categoryRepo, ImageServiceProxy imageServiceProxy) {
        this.productRepo = productRepo;
        this.inventoryRepo = inventoryRepo;
        this.sellerRepo = sellerRepo;
        this.categoryRepo = categoryRepo;
        this.imageServiceProxy = imageServiceProxy;
    }

    public Product getProduct(Long id) {
        return productRepo.findById(id).get();
    }

    private List<String> uploadAndGetIds(MultipartFile[] images) {
        return imageServiceProxy.uploadFiles(images);
//        return Arrays.stream(images).map(imageServiceProxy::uploadFile).collect(Collectors.toList());
    }

    public boolean addNewProduct(NewProduct newProduct, User user, Boolean isUpdate) {
        Set<Category> categories = new HashSet<>();
        newProduct.getCategories().forEach(x -> categories.add(categoryRepo.getCategoryByCategoryName(x)));
        Product product = Product.builder()
                .name(newProduct.getProduct_name())
                .description(newProduct.getProduct_description())
                .categories(categories)
                .build();
        if (isUpdate) {
            product.setId(newProduct.getProduct_id());
            if (newProduct.getImages() != null && newProduct.getImages().length > 0) {
                List<String> imageId = productRepo.findById(newProduct.getProduct_id()).get().getImageId();
                imageId.addAll(uploadAndGetIds(newProduct.getImages()));
                product.setImageId(imageId);
            }
        } else {
            if (newProduct.getImages() != null && newProduct.getImages().length > 0) {
                System.out.println();
                System.out.println("Images Received: " + newProduct.getImages().length);
                System.out.println();
                product.setImageId(uploadAndGetIds(newProduct.getImages()));
                System.out.println();
                System.out.println("Images Processed");
                System.out.println();
            } else {
                System.out.println("No images received");
            }
        }
        productRepo.save(product);
        Seller seller = user.seller;
        Inventory inventory;
        if (!isUpdate) {
            inventory = Inventory
                    .builder()
                    .quantity(newProduct.getQuantity())
                    .demand(0L)
                    .productId(product.getId())
                    .price(newProduct.getPrice())
                    .discount(newProduct.getDiscount())
                    .sellerId(seller.getId())
                    .build();
        } else {
            inventory = inventoryRepo.findById(new InventoryID(seller.getId(), newProduct.getProduct_id())).get();
            inventory.setDiscount(newProduct.getDiscount());
            inventory.setPrice(newProduct.getPrice());
            inventory.setQuantity(newProduct.getQuantity());
        }
        inventoryRepo.save(inventory);
        return true;
    }

    public List<PartialProduct> getPartialProducts(long id) {
        Session session = em.unwrap(Session.class);
        List resultList = session.createSQLQuery("select p.id,p.name,p.description from products p join inventory i on p.id=i.product_id where i.seller_id=:sellerId")
                .setParameter("sellerId", id)
                .addEntity(Product.class)
                .getResultList();
        List<PartialProduct> partialProducts = new ArrayList<>();
        resultList.forEach(x -> {
            Product x1 = (Product) x;
            partialProducts.add(new PartialProduct(x1.getId(), x1.getName()));
        });
        return partialProducts;
    }

    public List<PartialProduct> getPartialProductsExisting(long id) {
        Session session = em.unwrap(Session.class);
        List resultList = session.createSQLQuery("select distinct(p.id),p.name,p.description from products p join inventory i " +
                "on p.id=i.product_id where not exists(select i2.product_id from inventory i2 where i2.product_id=p.id and i2.seller_id=:sellerId)")
                .setParameter("sellerId", id)
                .addEntity(Product.class)
                .getResultList();
        List<PartialProduct> partialProducts = new ArrayList<>();
        resultList.forEach(x -> {
            Product x1 = (Product) x;
            partialProducts.add(new PartialProduct(x1.getId(), x1.getName()));
        });
        return partialProducts;
    }

    public boolean addExistingProduct(Inventory inventory, User user) {
        Seller seller = user.seller;
//        Inventory inventory;
        inventory.setSellerId(seller.getId());
        inventory.setDemand(0l);
        inventoryRepo.save(inventory);
        return true;
    }
}

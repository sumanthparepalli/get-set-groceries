package com.ecommerce.getsetgroceries.models;


import com.ecommerce.getsetgroceries.resultMappings.ProductInventorySeller;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@SqlResultSetMapping(
        name = "product-inventory-seller",
        classes = {
                @ConstructorResult(targetClass = ProductInventorySeller.class,
                        columns = {
                                @ColumnResult(name = "product_id", type = Long.class),
                                @ColumnResult(name = "product_name"),
                                @ColumnResult(name = "product_description"),
                                @ColumnResult(name = "seller_id", type = Long.class),
                                @ColumnResult(name = "seller_name"),
                                @ColumnResult(name = "price", type = Double.class),
                                @ColumnResult(name = "quantity", type = Integer.class),
                                @ColumnResult(name = "discount", type = Double.class),
                                @ColumnResult(name = "demand", type = Integer.class)
                        }
                )
        }
)
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "products_categories",
            joinColumns = @JoinColumn(name = "products_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<Category> categories;

    @ManyToMany(mappedBy = "products")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<Seller> sellers;

    @ElementCollection
    @JoinTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    public List<String> images;

    public Product(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Product() {
    }
}

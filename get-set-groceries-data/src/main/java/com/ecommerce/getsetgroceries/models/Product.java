package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
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

  public Product(long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Product() {
  }
}

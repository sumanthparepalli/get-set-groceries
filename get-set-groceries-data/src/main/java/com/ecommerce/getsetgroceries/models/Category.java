package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String categoryName;

  @ManyToMany(mappedBy = "categories")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  public Set<Product> products;

  public Category() {
  }

  public Category(String categoryName) {
    this.categoryName = categoryName;
  }
}

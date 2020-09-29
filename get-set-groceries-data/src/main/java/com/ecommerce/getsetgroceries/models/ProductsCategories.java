package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products_categories")
@IdClass(ProductsCategories.class)
@Data
public class ProductsCategories implements Serializable {

  @Id
  @Column(name = "products_id")
  private long productsId;
  @Id
  @Column(name = "categories_id")
  private long categoriesId;

  public ProductsCategories() {
  }

  public ProductsCategories(long productsId, long categoriesId) {
    this.productsId = productsId;
    this.categoriesId = categoriesId;
  }
}

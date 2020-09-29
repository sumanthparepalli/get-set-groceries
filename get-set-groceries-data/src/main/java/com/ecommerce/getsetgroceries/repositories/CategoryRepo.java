package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}

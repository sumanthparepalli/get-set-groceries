package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.Category;
import com.ecommerce.getsetgroceries.repositories.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<String> getAllCategoriesNames()
    {
        List<Category> categoryList = categoryRepo.findAll();
        List<String> names = new ArrayList<>();
        categoryList.forEach(x -> names.add(x.getCategoryName()));
        return names;
    }
}

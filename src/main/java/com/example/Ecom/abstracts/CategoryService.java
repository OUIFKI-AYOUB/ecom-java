package com.example.Ecom.abstracts;

import com.example.Ecom.dtos.CategoryCreate;
import com.example.Ecom.dtos.CategoryUpdate;
import com.example.Ecom.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> findAll();

    Category findOne(UUID categoryID);

    void deleteOne(UUID categoryID);

    Category createOne(CategoryCreate categoryCreate);

    Category updateOne(UUID categoryID, CategoryUpdate categoryUpdate);
}

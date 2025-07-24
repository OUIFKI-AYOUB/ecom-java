package com.example.Ecom.services;

import com.example.Ecom.abstracts.CategoryService;
import com.example.Ecom.dtos.CategoryCreate;
import com.example.Ecom.dtos.CategoryUpdate;
import com.example.Ecom.entities.Category;
import com.example.Ecom.reposiroties.CategoryRepo;
import com.example.Ecom.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }


    public Category findOne(UUID categoryID) {
        Category category = categoryRepo.findById(categoryID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Category with id " + categoryID + " not found"));

        return category;
    }


    @Override
    public void deleteOne(UUID categoryID) {
        categoryRepo.deleteById(categoryID);

    }


    public Category createOne(CategoryCreate categoryCreate) {
        Category category = new Category();

        category.setName(categoryCreate.name());


        categoryRepo.save(category);

        return category;

    }

    public Category updateOne(UUID categoryID, CategoryUpdate categoryUpdate) {

        Category category = categoryRepo.findById(categoryID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Category with id " + categoryID + " not found"));

        category.setName(categoryUpdate.name());

        return categoryRepo.save(category);
    }
}

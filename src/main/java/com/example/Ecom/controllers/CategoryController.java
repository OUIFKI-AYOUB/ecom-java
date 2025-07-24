package com.example.Ecom.controllers;



import com.example.Ecom.abstracts.CategoryService;
import com.example.Ecom.dtos.CategoryCreate;
import com.example.Ecom.dtos.CategoryUpdate;
import com.example.Ecom.entities.Category;
import com.example.Ecom.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")

public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @GetMapping

    public ResponseEntity<GlobalResponse<List<Category>>> findAll(){

        List<Category> categories = categoryService.findAll();

        return new ResponseEntity<>(new GlobalResponse<>(categories), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<GlobalResponse<Category>> findOne(@PathVariable UUID categoryId){

        Category category = categoryService.findOne(categoryId);

        return new ResponseEntity<>(new GlobalResponse<>(category), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")

    public ResponseEntity<Void> deleteOne(@PathVariable UUID categoryId){
        categoryService.deleteOne(categoryId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }


    @PostMapping
    public ResponseEntity<GlobalResponse<Category>> createOne(@RequestBody @Valid CategoryCreate category){

        Category newCategory = categoryService.createOne(category);

        return new ResponseEntity<>(new GlobalResponse<>(newCategory), HttpStatus.CREATED);
    }


    @PutMapping("/{categoryId}")
    public ResponseEntity<GlobalResponse<Category>> updateOne(@PathVariable UUID CategoryId,
                                                                @RequestBody @Valid CategoryUpdate category) {

        Category updatedCategory = categoryService.updateOne(CategoryId, category);

        return new ResponseEntity<>(new GlobalResponse<>(updatedCategory), HttpStatus.OK);

    }

}

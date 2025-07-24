package com.example.Ecom.services;

import com.example.Ecom.abstracts.ProductService;
import com.example.Ecom.dtos.ProductCreate;
import com.example.Ecom.dtos.ProductUpdate;
import com.example.Ecom.entities.Category;
import com.example.Ecom.entities.Product;
import com.example.Ecom.reposiroties.CategoryRepo;
import com.example.Ecom.reposiroties.ProductRepo;
import com.example.Ecom.shared.CustomResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;


    @Override
    public Page<Product> findAll(int page, int size ,String sortBy, String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepo.findAll(pageable);
    }

    @Override
    public Product findOne(UUID productID) {
        return productRepo.findById(productID)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Product not found"));
    }


    public void deleteOne(UUID productID) {
        productRepo.deleteById(productID);
    }






    public Product createOne(ProductCreate dto) {
        Product product = new Product();

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());

        if (dto.categoryIds() == null || dto.categoryIds().isEmpty()) {
            throw CustomResponseException.BadRequest("At least one category must be provided");
        }

        Set<Category> categories = new HashSet<>(categoryRepo.findAllById(dto.categoryIds()));

        if (categories.isEmpty()) {
            throw CustomResponseException.BadRequest("Invalid category IDs provided");
        }

        if (categories.size() != dto.categoryIds().size()) {
            throw CustomResponseException.BadRequest("Some category IDs do not exist");
        }

        product.setCategories(categories);


        Product saved = productRepo.save(product);

        return saved;
    }


    public Product updateOne(UUID productID, ProductUpdate dto) {
        Product product = productRepo.findById(productID)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Product not found"));

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());

        if (dto.categoryIds() == null || dto.categoryIds().isEmpty()) {
            throw CustomResponseException.BadRequest("At least one category must be provided");
        }

        Set<Category> categories = new HashSet<>(categoryRepo.findAllById(dto.categoryIds()));

        if (categories.isEmpty()) {
            throw CustomResponseException.BadRequest("Invalid category IDs provided");
        }

        if (categories.size() != dto.categoryIds().size()) {
            throw CustomResponseException.BadRequest("Some category IDs do not exist");
        }

        product.setCategories(categories);


        return productRepo.save(product);
    }


    public List<Product> SearchProducts(String name) {
        return productRepo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Product createWithFiles(ProductCreate productCreate, List<MultipartFile> files) {
        return null;
    }


}

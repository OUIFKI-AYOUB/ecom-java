package com.example.Ecom.abstracts;

import com.example.Ecom.dtos.CategoryCreate;
import com.example.Ecom.dtos.CategoryUpdate;
import com.example.Ecom.dtos.ProductCreate;
import com.example.Ecom.dtos.ProductUpdate;
import com.example.Ecom.entities.Category;
import com.example.Ecom.entities.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Page<Product> findAll(int page, int size ,String sortBy, String direction);

    Product findOne(UUID productID);

    void deleteOne(UUID productID);

    Product createOne(ProductCreate productCreate);

    Product updateOne(UUID productID, ProductUpdate productUpdate);

    List<Product> SearchProducts(String name);

    Product createWithFiles(@Valid ProductCreate productCreate, List<MultipartFile> files);
}

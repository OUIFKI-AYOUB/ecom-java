package com.example.Ecom.controllers;

import com.example.Ecom.abstracts.ProductService;
import com.example.Ecom.dtos.PaginatedResponse;
import com.example.Ecom.dtos.ProductCreate;
import com.example.Ecom.dtos.ProductUpdate;
import com.example.Ecom.entities.Product;
import com.example.Ecom.shared.GlobalResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Get all products
    @GetMapping
    public ResponseEntity<GlobalResponse<PaginatedResponse<Product>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            HttpServletRequest request
    ) {
        Page<Product> products = productService.findAll(page, size, sortBy, direction);

        String baseUrl = request.getRequestURL().toString();
        String nextUrl = products.hasNext() ? String.format("%s?page=%d&size=%d&sortBy=%s&direction=%s", baseUrl, page + 1, size, sortBy, direction) : null;
        String previousUrl = products.hasPrevious() ? String.format("%s?page=%d&size=%d&sortBy=%s&direction=%s", baseUrl, page - 1, size, sortBy, direction) : null;

        var paginatedResponse = new PaginatedResponse<Product>(
                products.getContent(),
                products.getNumber(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.hasNext(),
                products.hasPrevious(),
                nextUrl,
                previousUrl

        );

        return new ResponseEntity<>(new GlobalResponse<>(paginatedResponse), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<GlobalResponse<List<Product>>> SearchProducts(@RequestParam  String name) {
        List<Product> products = productService.SearchProducts(name);
        return new ResponseEntity<>(new GlobalResponse<>(products), HttpStatus.OK);
    }

    // Get one product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<GlobalResponse<Product>> findOne(@PathVariable UUID productId) {
        Product product = productService.findOne(productId);
        return new ResponseEntity<>(new GlobalResponse<>(product), HttpStatus.OK);
    }

    // Delete a product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID productId) {
        productService.deleteOne(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Create a product
    @PostMapping
    public ResponseEntity<GlobalResponse<Product>> createOne(@RequestBody @Valid ProductCreate productCreate) {
        Product newProduct = productService.createOne(productCreate);
        return new ResponseEntity<>(new GlobalResponse<>(newProduct), HttpStatus.CREATED);
    }

    // Update a product
    @PutMapping("/{productId}")
    public ResponseEntity<GlobalResponse<Product>> updateOne(@PathVariable UUID productId,
                                                             @RequestBody @Valid ProductUpdate productUpdate) {
        Product updatedProduct = productService.updateOne(productId, productUpdate);
        return new ResponseEntity<>(new GlobalResponse<>(updatedProduct), HttpStatus.OK);
    }



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse<Product>> createWithFiles(
            @RequestPart("product") @Valid ProductCreate productCreate,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        // Implement file handling logic here
        Product newProduct = productService.createWithFiles(productCreate, files);
        return new ResponseEntity<>(new GlobalResponse<>(newProduct), HttpStatus.CREATED);
    }
}

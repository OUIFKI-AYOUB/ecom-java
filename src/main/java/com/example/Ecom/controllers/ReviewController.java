package com.example.Ecom.controllers;



import com.example.Ecom.abstracts.CategoryService;
import com.example.Ecom.abstracts.ReviewService;
import com.example.Ecom.dtos.CategoryCreate;
import com.example.Ecom.dtos.CategoryUpdate;
import com.example.Ecom.dtos.ReviewCreate;
import com.example.Ecom.dtos.ReviewUpdate;
import com.example.Ecom.entities.Category;
import com.example.Ecom.entities.Reviews;
import com.example.Ecom.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")

public class ReviewController {


    @Autowired
    private ReviewService reviewService;


    @GetMapping

    public ResponseEntity<GlobalResponse<List<Reviews>>> findAll(){

        List<Reviews> reviews = reviewService.findAll();

        return new ResponseEntity<>(new GlobalResponse<>(reviews), HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<GlobalResponse<Reviews>> findOne(@PathVariable UUID reviewId){

        Reviews review = reviewService.findOne(reviewId);

        return new ResponseEntity<>(new GlobalResponse<>(review), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")

    public ResponseEntity<Void> deleteOne(@PathVariable UUID reviewId){
        reviewService.deleteOne(reviewId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }


    @PostMapping
    public ResponseEntity<GlobalResponse<Reviews>> createOne(@RequestBody @Valid ReviewCreate review){

        Reviews newReview = reviewService.createOne(review);

        return new ResponseEntity<>(new GlobalResponse<>(newReview), HttpStatus.CREATED);
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<GlobalResponse<Reviews>> updateOne(@PathVariable UUID reviewId,
                                                                @RequestBody @Valid ReviewUpdate review) {

        Reviews reviewUpdate = reviewService.updateOne(reviewId, review);

        return new ResponseEntity<>(new GlobalResponse<>(reviewUpdate), HttpStatus.OK);

    }

}

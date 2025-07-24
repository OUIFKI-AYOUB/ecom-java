package com.example.Ecom.abstracts;

import com.example.Ecom.dtos.CategoryCreate;
import com.example.Ecom.dtos.CategoryUpdate;
import com.example.Ecom.dtos.ReviewCreate;
import com.example.Ecom.dtos.ReviewUpdate;
import com.example.Ecom.entities.Category;
import com.example.Ecom.entities.Reviews;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    List<Reviews> findAll();

    Reviews findOne(UUID reviewID);

    void deleteOne(UUID reviewID);

    Reviews createOne(ReviewCreate reviewCreate);

    Reviews updateOne(UUID reviewID, ReviewUpdate reviewUpdate);
}

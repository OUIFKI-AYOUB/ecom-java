package com.example.Ecom.services;

import com.example.Ecom.abstracts.ReviewService;
import com.example.Ecom.dtos.ReviewCreate;
import com.example.Ecom.dtos.ReviewUpdate;
import com.example.Ecom.entities.Product;
import com.example.Ecom.entities.Reviews;
import com.example.Ecom.entities.Role;
import com.example.Ecom.entities.Users;
import com.example.Ecom.reposiroties.ProductRepo;
import com.example.Ecom.reposiroties.ReviewRepo;
import com.example.Ecom.reposiroties.UserRepo;
import com.example.Ecom.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    // Helper to get current authenticated user from Spring Security
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findOneByEmail(email)
                .orElseThrow(() -> CustomResponseException
                        .Unauthorized("User not found or not authenticated"));
    }

    // Helper to check if current user is ADMIN
    private boolean isAdmin(Users user) {
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public List<Reviews> findAll() {
        return reviewRepo.findAll();
    }


    @Override
    public Reviews findOne(UUID reviewID) {
        Reviews review = reviewRepo.findById(reviewID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Review with ID " + reviewID + " not found"));

        return review;
    }

    @Override
    public void deleteOne(UUID reviewID) {

        Reviews review = reviewRepo.findById(reviewID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Review with ID " + reviewID + " not found"));

        Users currentUser = getCurrentUser();

        if (!isAdmin(currentUser) && !review.getUser().getId().equals(currentUser.getId())) {
            throw CustomResponseException.Unauthorized("You are not allowed to delete this review.");
        }

        reviewRepo.deleteById(reviewID);

    }

    @Override
    public Reviews createOne(ReviewCreate reviewCreate) {

        Product product = productRepo.findById(reviewCreate.productId()).
                orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Product with ID " + reviewCreate.productId() + " not found"));

        Users user = getCurrentUser();

        Reviews review = new Reviews();
        review.setRating(reviewCreate.rating());
        review.setComment(reviewCreate.comment());
        review.setUser(user);
        review.setProduct(product);
        return reviewRepo.save(review);
    }

    @Override
    public Reviews updateOne(UUID reviewID, ReviewUpdate reviewUpdate) {

        Reviews review = reviewRepo.findById(reviewID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Review with ID " + reviewID + " not found"));

        Users currentUser = getCurrentUser();

        // Check if user is ADMIN or owner of the review
        if (!isAdmin(currentUser) && !review.getUser().getId().equals(currentUser.getId())) {
            throw CustomResponseException.Unauthorized("You are not allowed to update this review.");
        }
        review.setRating(reviewUpdate.rating());
        review.setComment(reviewUpdate.comment());
        return reviewRepo.save(review);
    }
}

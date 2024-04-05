package com.example.review.repository;

import com.example.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByPurchaseFormId(Long purchaseFormId);
}

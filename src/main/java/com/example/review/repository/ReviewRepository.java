package com.example.review.repository;

import com.example.member.entity.Member;
import com.example.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByPurchaseFormId(Long purchaseFormId);

    int countBySellerAndSellerKindnessIsTrue(Member seller);

    int countBySellerAndGoodNotificationIsTrue(Member seller);

    int countBySellerAndDescriptionMatchIsTrue(Member seller);

    int countBySellerAndArrivalSatisfactoryIsTrue(Member seller);
}

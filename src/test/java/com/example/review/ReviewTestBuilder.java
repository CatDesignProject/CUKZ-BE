package com.example.review;

import com.example.member.entity.Member;
import com.example.product.ProductTestBuilder;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.review.dto.request.ReviewRequestDto;
import com.example.review.entity.Review;


public class ReviewTestBuilder {

    public static ReviewRequestDto testReviewRequestDto() {
        return new ReviewRequestDto(true, true, false, false);
    }

    public static PurchaseForm testPurchaseForm() {
        Member member = ProductTestBuilder.testMemberBuild(); //buyer 1L
        Product product = ProductTestBuilder.testProduct2Build();
        product.updateStatus(SaleStatus.COMPLETE);

        return PurchaseForm.builder()
                .id(1L)
                .address("역곡")
                .payStatus(true)
                .product(product)
                .memberId(member.getId())
                .build();
    }

    public static PurchaseForm testPurchaseForm2() {
        Member member = ProductTestBuilder.testMemberBuild(); //buyer 1L
        Product product = ProductTestBuilder.testProduct2Build();
        return PurchaseForm.builder()
                .id(1L)
                .payStatus(true)
                .product(product)
                .build();
    }

    public static Review testReview() {
        PurchaseForm purchaseForm = testPurchaseForm();
        return Review.builder()
                .id(1L)
                .sellerKindness(true)
                .descriptionMatch(true)
                .arrivalSatisfactory(false)
                .goodNotification(false)
                .purchaseFormId(purchaseForm.getId())
                .buyerId(purchaseForm.getMemberId())
                .seller(testSeller())
                .build();
    }

    public static Member testSeller() {
        return ProductTestBuilder.testMember2Build(); //seller 2L
    }
}

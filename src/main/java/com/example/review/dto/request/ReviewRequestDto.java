package com.example.review.dto.request;

import com.example.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {
    private boolean sellerKindness;
    private boolean goodNotification;
    private boolean arrivalSatisfactory;
    private boolean descriptionMatch;

    public Review toReview() {
        return Review.builder()
                .sellerKindness(this.sellerKindness)
                .goodNotification(this.goodNotification)
                .arrivalSatisfactory(this.arrivalSatisfactory)
                .descriptionMatch(this.descriptionMatch)
                .build();
    }
}

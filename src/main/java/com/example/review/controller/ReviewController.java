package com.example.review.controller;

import com.example.common.global.BaseResponse;
import com.example.review.dto.request.ReviewRequestDto;
import com.example.review.dto.response.ReviewResponseDto;
import com.example.review.service.ReviewService;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/member/{sellerId}/purchaseForm/{purchaseFormId}")
    public ResponseEntity<BaseResponse<String>> saveReview(@RequestBody ReviewRequestDto reviewRequestDto, @PathVariable Long sellerId, @PathVariable Long purchaseFormId, @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        reviewService.saveReview(reviewRequestDto, sellerId, purchaseFormId, authenticatedMember.getMemberId());
        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, "리뷰 완료"));
    }

    @GetMapping("/member/{sellerId}")
    public ResponseEntity<BaseResponse<ReviewResponseDto>> findReview(@PathVariable Long sellerId) {
        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, reviewService.findReview(sellerId)));
    }
}

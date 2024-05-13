package com.example.review.controller;

import com.example.review.dto.request.ReviewRequestDto;
import com.example.review.dto.response.ReviewResponseDto;
import com.example.review.service.ReviewService;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews/members/{sellerId}")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ReviewResponseDto> findReview(@PathVariable Long sellerId) {
        return ResponseEntity.ok().body(reviewService.findReview(sellerId));
    }

    @PostMapping("/purchaseForm/{purchaseFormId}")
    public ResponseEntity<String> saveReview(@RequestBody ReviewRequestDto reviewRequestDto
            , @PathVariable Long sellerId, @PathVariable Long purchaseFormId
            , @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        reviewService.saveReview(reviewRequestDto, sellerId, purchaseFormId, authenticatedMember.getMemberId());
        return ResponseEntity.ok().body("리뷰 완료");
    }
}
package com.example.review.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.repository.PurchaseFormRepository;
import com.example.review.dto.request.ReviewRequestDto;
import com.example.review.entity.Review;
import com.example.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PurchaseFormRepository purchaseFormRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveReview(ReviewRequestDto reviewRequestDto, Long sellerId, Long purchaseFormId, Long buyerId) {
        //해당 상품이 공구가 완료된 상품인지
        PurchaseForm purchaseForm = purchaseFormRepository.findById(purchaseFormId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_PURCHASE_FORM)
                );
        Product product = purchaseForm.getProduct();
        if (!product.getStatus().equals(SaleStatus.COMPLETE)) {
            throw new GlobalException(BaseErrorCode.NOT_YET_WRITE_REVIEW);
        }

        //해당 purchaseForm에 대해 리뷰를 작성했는지 확인 -> 이미 작성 했으면 예외처리
        reviewRepository.findByPurchaseFormId(purchaseFormId).ifPresent(review -> {
            throw new GlobalException(BaseErrorCode.ALREADY_WRITE_REVIEW);
        });

        //purchaseFormId의 구매폼이 sellerId가 등록한 상품에 대한 구매폼인지 확인
        if (!purchaseForm.getProduct().getMember().getId().equals(sellerId)) {
            throw new GlobalException(BaseErrorCode.UNAUTHORIZED_WRITE_REVIEW_TO_SELLER);
        }

        // 리뷰 작성
        Review review = reviewRequestDto.toReview();

        Member member = memberRepository.findById(sellerId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_MEMBER)
                );

        review.addSeller(member);
        review.addBuyerId(buyerId);
        review.addPurchaseFormId(purchaseFormId);
        reviewRepository.save(review);
    }
}







//        PurchaseForm purchaseForm = purchaseFormRepository.findByIdAndMemberId(purchaseFormId, sellerId)
//                .orElseThrow(
//                () -> new GlobalException(BaseErrorCode.UNAUTHORIZED_WRITE_REVIEW_TO_SELLER)
//        );
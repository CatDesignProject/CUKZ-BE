package com.example.review.service;

import com.example.common.exception.GlobalException;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.repository.PurchaseFormRepository;
import com.example.review.ReviewTestBuilder;
import com.example.review.dto.request.ReviewRequestDto;
import com.example.review.entity.Review;
import com.example.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService 클래스의")
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    PurchaseFormRepository purchaseFormRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    ReviewService reviewService;

    PurchaseForm purchaseForm =ReviewTestBuilder.testPurchaseForm();
    ReviewRequestDto reviewRequestDto = ReviewTestBuilder.testReviewRequestDto();
    Member buyer = purchaseForm.getMember();
    Member seller = ReviewTestBuilder.testSeller();
    Review review;
    Long otherSellerId = 3L;
    @Nested
    @DisplayName("saveReview 메서드는")
    class Describe_saveReview {
        @Nested
        @DisplayName("seller의 pk값과 purchaseForm의 pk값이 주어진 경우")
        class Context_with_sellerId_and_purchaseFormId {
            @BeforeEach
            void setUp() {
                given(purchaseFormRepository.findById(purchaseForm.getId())).willReturn(Optional.of(purchaseForm));
                given(reviewRepository.findByPurchaseFormId(purchaseForm.getId())).willReturn(Optional.empty());
                given(memberRepository.findById(seller.getId())).willReturn(Optional.of(seller));
                given(reviewRepository.save(any(Review.class))).willReturn(any(Review.class));

                reviewService.saveReview(reviewRequestDto, seller.getId(), purchaseForm.getId(), buyer.getId());
            }
            @Test
            @DisplayName("리뷰 작성에 성공한다.")
            void it_returns_succes_save_review() {
                then(purchaseFormRepository).should(times(1)).findById(purchaseForm.getId());
                then(reviewRepository).should(times(1)).findByPurchaseFormId(purchaseForm.getId());
                then(memberRepository).should(times(1)).findById(seller.getId());
                then(reviewRepository).should(times(1)).save(any(Review.class));
            }
        }
        @Nested
        @DisplayName("이미 해당 구매폼이 리뷰를 작성한 경우")
        class Context_with_already_write_from_purchaseFormId {
            @BeforeEach
            void setUp() {
                review = ReviewTestBuilder.testReview();

                given(purchaseFormRepository.findById(purchaseForm.getId())).willReturn(Optional.of(purchaseForm));
                given(reviewRepository.findByPurchaseFormId(purchaseForm.getId())).willReturn(Optional.of(review));
            }
            @Test
            @DisplayName("이미 리뷰를 작성했다는 예외를 발생시킨다.")
            void it_returns_already_write_review() {
                assertThatThrownBy(() -> reviewService.saveReview(reviewRequestDto, seller.getId(), purchaseForm.getId(), buyer.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("이미 리뷰를 작성했습니다.");
            }
        }
        @Nested
        @DisplayName("해당 구매폼이 sellerId 총대가 공구를 진행한 물품이 아닌 경우")
        class Context_with_purchaseForm_not_match_seller {
            @BeforeEach
            void setUp() {
                given(reviewRepository.findByPurchaseFormId(purchaseForm.getId())).willReturn(Optional.empty());
                given(purchaseFormRepository.findById(purchaseForm.getId())).willReturn(Optional.of(purchaseForm));
            }
            @Test
            @DisplayName("해당 총대에게 리뷰를 작성할 권한 없음 예외를 발생시킨다.")
            void it_returns_unauthorized_write_review_to_seller() {
                assertThatThrownBy(() -> reviewService.saveReview(reviewRequestDto, otherSellerId, purchaseForm.getId(), buyer.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 총대에게 리뷰를 작성할 권한이 없습니다.");
            }
        }
        @Nested
        @DisplayName("구매폼이 없는 경우")
        class Context_with_not_exist_purchaseForm {
            @BeforeEach
            void setUp() {
                given(purchaseFormRepository.findById(1L)).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("해당 구매폼을 찾을 수 없다는 예외를 발생시킨다.")
            void it_returns_not_found_purchase_form() {
                assertThatThrownBy(() -> reviewService.saveReview(reviewRequestDto, seller.getId(), purchaseForm.getId(), buyer.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 구매폼을 찾을 수 없습니다.");
            }
        }
        @Nested
        @DisplayName("공구가 완료되지 않아서 아직 리뷰 작성 기간이 아닌 경우")
        class Context_with_sale_status_not_complete {
            @BeforeEach
            void setUp() {
                purchaseForm = ReviewTestBuilder.testPurchaseForm2();
                given(purchaseFormRepository.findById(purchaseForm.getId())).willReturn(Optional.of(purchaseForm));
            }
            @Test
            @DisplayName("아직 리뷰 작성이 불가능한 상품 예외를 발생시킨다.")
            void it_returns_not_yet_write_review() {
                assertThatThrownBy(() -> reviewService.saveReview(reviewRequestDto, seller.getId(), purchaseForm.getId(), buyer.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("아직 해당 상품은 리뷰 작성을 할 수 없습니다.");
            }
        }
    }
}
package com.example.purchaseForm.service;

import com.example.common.exception.GlobalException;
import com.example.demandForm.DemandTestBuilder;
import com.example.product.ProductTestBuilder;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.purchaseForm.PurchaseTest;
import com.example.purchaseForm.PurchaseTestBuilder;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import com.example.purchaseForm.dto.PurchaseFormResponseDto;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;
import com.example.purchaseForm.repository.PurchaseFormRepository;
import com.example.purchaseForm.repository.PurchaseOptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.common.exception.BaseErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseFormServiceTest implements PurchaseTest {

    @Mock
    PurchaseFormRepository purchaseFormRepository;
    @Mock
    PurchaseOptionRepository purchaseOptionRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    OptionRepository optionRepository;
    @InjectMocks
    PurchaseFormService purchaseFormService;

    Product product;
    PurchaseForm purchaseForm;
    Option option;
    PurchaseOption purchaseOption;
    PurchaseFormRequestDto requestDto;
    Long productId = 1L;
    Long memberId = 1L;

    @BeforeEach
    void setUp() {
        product = ProductTestBuilder.testProductBuild();
        ReflectionTestUtils.setField(product, "id", productId);

        option = DemandTestBuilder.buildOption(ADDITIONAL_PRICE);
        purchaseOption = PurchaseTestBuilder.buildPurchaseOption();
        purchaseForm = PurchaseTestBuilder.buildPurchaseForm();
        requestDto = PurchaseTestBuilder.buildPurchaseFormRequestDto();
    }

    @Nested
    @DisplayName("구매 참여 테스트")
    class purchaseTest {

        @Test
        @DisplayName("성공(일반 유저)")
        void purchaseMemberTest_success() {
            // given
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(purchaseFormRepository.save(any())).thenReturn(purchaseForm);
            when(optionRepository.findById(any())).thenReturn(Optional.of(option));
            when(purchaseOptionRepository.save(any())).thenReturn(purchaseOption);

            // when
            PurchaseFormResponseDto responseDto = purchaseFormService.purchaseMember(productId, requestDto, memberId);
            int totalPrice = (product.getPrice() + option.getAdditionalPrice()) * 2;

            // then
            assertEquals(memberId, responseDto.getMemberId());
            assertEquals(product.getId(), responseDto.getProductId());
            assertEquals(option.getId(), responseDto.getOptionList().get(0).getOptionId());
            assertEquals(totalPrice, responseDto.getTotalPrice());
        }

        @Test
        @DisplayName("실패(일반 유저) - 상품 없음")
        void purchaseFormTest_fail_notFoundProduct() {
            // given
            when(productRepository.findById(any())).thenReturn(Optional.empty());

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.purchaseMember(productId, requestDto, memberId);
            });
            assertEquals(NOT_FOUND_PRODUCT, e.getErrorCode());
        }

        @Test
        @DisplayName("실패(일반 유저) - 옵션 없음")
        void purchaseFormTest_fail_notFoundOption() {
            // given
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(purchaseFormRepository.save(any())).thenReturn(purchaseForm);
            when(optionRepository.findById(any())).thenReturn(Optional.empty());

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.purchaseMember(productId, requestDto, memberId);
            });
            assertEquals(NOT_FOUND_OPTION, e.getErrorCode());
        }

        @Test
        @DisplayName("실패(일반 유저) - 참여 기간이 아님")
        void purchaseFormTest_fail_notInPeriod() {
            // given
            LocalDateTime endDate = LocalDateTime.of(2000, 4, 1, 12, 0);
            ReflectionTestUtils.setField(product, "endDate", endDate);

            when(productRepository.findById(any())).thenReturn(Optional.of(product));

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.purchaseMember(productId, requestDto, memberId);
            });
            assertEquals(NOT_IN_PERIOD, e.getErrorCode());
        }

        @Test
        @DisplayName("성공(비회원)")
        void purchaseNonMemberTest_success() {
            // given
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(purchaseFormRepository.save(any())).thenReturn(purchaseForm);
            when(optionRepository.findById(any())).thenReturn(Optional.of(option));
            when(purchaseOptionRepository.save(any())).thenReturn(purchaseOption);

            // when
            PurchaseFormResponseDto responseDto = purchaseFormService.purchaseNonMember(productId, requestDto);
            int totalPrice = (product.getPrice() + option.getAdditionalPrice()) * 2;

            // then
            assertEquals(product.getId(), responseDto.getProductId());
            assertEquals(option.getId(), responseDto.getOptionList().get(0).getOptionId());
            assertEquals(totalPrice, responseDto.getTotalPrice());
        }
    }
}
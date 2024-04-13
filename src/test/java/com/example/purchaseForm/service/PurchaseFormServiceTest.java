package com.example.purchaseForm.service;

import com.example.common.exception.GlobalException;
import com.example.demandForm.DemandTestBuilder;
import com.example.demandForm.dto.request.GetFormNonMemberRequestDto;
import com.example.product.ProductTestBuilder;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.purchaseForm.PurchaseTest;
import com.example.purchaseForm.PurchaseTestBuilder;
import com.example.purchaseForm.dto.PayRequestDto;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import com.example.purchaseForm.dto.PurchaseFormResponseDto;
import com.example.purchaseForm.entity.Delivery;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;
import com.example.purchaseForm.repository.DeliveryRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    @Mock
    DeliveryRepository deliveryRepository;
    @InjectMocks
    PurchaseFormService purchaseFormService;

    Product product;
    PurchaseForm purchaseForm;
    Option option;
    PurchaseOption purchaseOption;
    PurchaseFormRequestDto requestDto;
    Delivery delivery;
    Long productId = 1L;
    Long memberId = 1L;
    int page = 1;
    int size = 10;

    @BeforeEach
    void setUp() {
        product = ProductTestBuilder.testProductBuild();
        ReflectionTestUtils.setField(product, "id", productId);

        option = DemandTestBuilder.buildOption(ADDITIONAL_PRICE);
        purchaseOption = PurchaseTestBuilder.buildPurchaseOption();
        purchaseForm = PurchaseTestBuilder.buildPurchaseForm();
        requestDto = PurchaseTestBuilder.buildPurchaseFormRequestDto();
        delivery = PurchaseTestBuilder.buildDelivery(product);
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
            when(deliveryRepository.findById(any())).thenReturn(Optional.of(delivery));

            // when
            PurchaseFormResponseDto responseDto = purchaseFormService.purchaseMember(productId, requestDto, memberId);
            int totalPrice = (product.getPrice() + option.getAdditionalPrice()) * 2 + DELIVERY_PRICE;

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
            when(deliveryRepository.findById(any())).thenReturn(Optional.of(delivery));

            // when
            PurchaseFormResponseDto responseDto = purchaseFormService.purchaseNonMember(productId, requestDto);
            int totalPrice = (product.getPrice() + option.getAdditionalPrice()) * 2 + DELIVERY_PRICE;

            // then
            assertEquals(product.getId(), responseDto.getProductId());
            assertEquals(option.getId(), responseDto.getOptionList().get(0).getOptionId());
            assertEquals(totalPrice, responseDto.getTotalPrice());
        }
    }

    @Nested
    @DisplayName("구매 폼 조회 테스트")
    class getPurchaseFormTest {
        @Test
        @DisplayName("성공(일반 유저) - 단건 조회")
        void getMemberPurchaseFormTest_success() {
            // given
            ReflectionTestUtils.setField(purchaseForm, "id", 1L);
            when(purchaseFormRepository.findById(any())).thenReturn(Optional.of(purchaseForm));

            // when
            PurchaseFormResponseDto responseDto = purchaseFormService.getPurchaseFormMember(purchaseForm.getId(), memberId);

            // then
            assertEquals(purchaseForm.getId(), responseDto.getId());
            assertEquals(purchaseForm.getMemberId(), responseDto.getMemberId());
            assertEquals(purchaseForm.getProduct().getId(), responseDto.getProductId());
        }

        @Test
        @DisplayName("실패(일반 유저) - 작성자와 불일치")
        void Test() {
            // given
            Long memberId2 = 2L;
            when(purchaseFormRepository.findById(any())).thenReturn(Optional.of(purchaseForm));

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.getPurchaseFormMember(purchaseForm.getId(), memberId2);
            });
            assertEquals(UNAUTHORIZED_MEMBER, e.getErrorCode());
        }

        @Test
        @DisplayName("성공(비회원) - 단건 조회")
        void getNonMemberPurchaseFormTest_success() {
            // given
            Long orderNumber = 12435L;
            GetFormNonMemberRequestDto nonMemberRequestDto = new GetFormNonMemberRequestDto(orderNumber);
            ReflectionTestUtils.setField(purchaseForm, "memberId", orderNumber);

            when(purchaseFormRepository.findByOrderNumber(any())).thenReturn(Optional.of(purchaseForm));

            // when
            PurchaseFormResponseDto responseDto = purchaseFormService.getPurchaseFormNonMember(nonMemberRequestDto);

            // then
            assertEquals(purchaseForm.getId(), responseDto.getId());
            assertEquals(purchaseForm.getMemberId(), responseDto.getMemberId());
            assertEquals(purchaseForm.getProduct().getId(), responseDto.getProductId());
        }

        @Test
        @DisplayName("성공(일반 유저) - 페이징 조회")
        void getAllPurchaseFormMemberTest_success() {
            // given
            PurchaseForm purchaseForm2 = PurchaseForm.toEntity(memberId, product, PurchaseTestBuilder.buildPurchaseFormRequestDto());
            List<PurchaseForm> purchaseFormList = Arrays.asList(purchaseForm, purchaseForm2);
            Page<PurchaseForm> purchaseFormPage = new PageImpl<>(purchaseFormList);

            when(purchaseFormRepository.findByMemberId(any(), any())).thenReturn(purchaseFormPage);

            // when
            Page<PurchaseFormResponseDto> responseDtoPage = purchaseFormService.getAllPurchaseFormsMember(page, size, memberId);

            // then
            assertEquals(purchaseFormList.size(), responseDtoPage.getContent().size());
            assertEquals(purchaseFormPage.getTotalPages(), responseDtoPage.getTotalPages());
            assertEquals(purchaseFormList.get(0).getId(), responseDtoPage.getContent().get(0).getId());
        }

        @Test
        @DisplayName("성공(총대) - 페이징 조회")
        void getAllPurchaseFormsTest_success() {
            // given
            PurchaseForm purchaseForm2 = PurchaseForm.toEntity(memberId, product, PurchaseTestBuilder.buildPurchaseFormRequestDto());
            List<PurchaseForm> purchaseFormList = Arrays.asList(purchaseForm, purchaseForm2);
            Page<PurchaseForm> purchaseFormPage = new PageImpl<>(purchaseFormList);

            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(purchaseFormRepository.findByProductId(any(), any())).thenReturn(purchaseFormPage);

            // when
            Page<PurchaseFormResponseDto> responseDtoPage = purchaseFormService.getAllPurchaseForms(page, size, productId, memberId);

            // then
            assertEquals(purchaseFormList.size(), responseDtoPage.getContent().size());
            assertEquals(purchaseFormPage.getTotalPages(), responseDtoPage.getTotalPages());
            assertEquals(purchaseFormList.get(0).getId(), responseDtoPage.getContent().get(0).getId());
        }

        @Test
        @DisplayName("실패(총대) - 권한 없음")
        void getAllPurchaseFormsTest_fail_unauthorized() {
            // given
            Long memberId2 = 2L;
            when(productRepository.findById(any())).thenReturn(Optional.of(product));

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.getAllPurchaseForms(page, size, productId, memberId2);
            });
            assertEquals(UNAUTHORIZED_MEMBER, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("입금 상태 변경 테스트")
    class updatePayStatusTest {
        PayRequestDto payRequestDto = PayRequestDto.builder()
                .payStatus(true)
                .purchaseFormIds(List.of(1L, 2L))
                .build();

        @Test
        @DisplayName("성공")
        void updatePayStatusTest_success() {
            // given
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(purchaseFormRepository.findById(any())).thenReturn(Optional.of(purchaseForm));

            // when
            purchaseFormService.updatePayStatus(purchaseForm.getId(), payRequestDto, memberId);

            // then
            assertEquals(true, purchaseForm.getPayStatus());
        }

        @Test
        @DisplayName("실패 - 구매 폼 없음")
        void updatePayStatusTest_fail_notFound() {
            // given
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(purchaseFormRepository.findById(any())).thenReturn(Optional.empty());

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.updatePayStatus(purchaseForm.getId(), payRequestDto, memberId);
            });
            assertEquals(NOT_FOUND_FORM, e.getErrorCode());
        }

        @Test
        @DisplayName("실패 - 권한 없음")
        void updatePayStatusTest_fail_unauthorized() {
            // given
            Long memberId2 = 2L;
            when(productRepository.findById(any())).thenReturn(Optional.of(product));

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseFormService.updatePayStatus(purchaseForm.getId(), payRequestDto, memberId2);
            });
            assertEquals(UNAUTHORIZED_MEMBER, e.getErrorCode());

        }
    }
}
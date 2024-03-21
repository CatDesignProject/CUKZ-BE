package com.example.demandForm.service;

import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DemandFormServiceTest {

    @Mock
    DemandFormRepository demandFormRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    DemandFormService demandFormService;

    Member member;
    Product product;
    DemandForm memberDemandForm;
    DemandForm nonMemberDemandForm;
    DemandFormRequestDto requestDto;
    Long productId = 1L;
    Long memberId = 1L;
    int quantity = 3;

    @BeforeEach
    void setUp() {
        member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L);

        LocalDateTime endDate = LocalDateTime.of(2024, 3, 25, 12, 30);
        product = new Product(1L, "상품", 10000, "정보", LocalDateTime.now(), endDate);

        requestDto = new DemandFormRequestDto(quantity);
        memberDemandForm = DemandForm.toMemberEntity(member, product, requestDto);
        nonMemberDemandForm = DemandForm.toNonMemberEntity(1L, product, requestDto);
    }

    @Nested
    @DisplayName("수요조사 참여 테스트")
    class demandTest {

        @Test
        @DisplayName("성공(일반 유저)")
        void demandMemberTest_success() {
            // given
            when(demandFormRepository.findByProductIdAndMemberId(productId, memberId)).thenReturn(Optional.empty());
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(demandFormRepository.save(any(DemandForm.class))).thenReturn(memberDemandForm);

            // when
            DemandFormResponseDto responseDto = demandFormService.demandMember(productId, requestDto, member);

            // then
            assertEquals(quantity, responseDto.getQuantity());
            assertEquals(productId, responseDto.getProductId());
        }

        @Test
        @DisplayName("실패(일반 유저) - 중복 참여")
        void demandMemberTest_fail_duplicate() {
            // given
            when(demandFormRepository.findByProductIdAndMemberId(productId, memberId)).thenReturn(Optional.of(memberDemandForm));

            // when - then
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
                demandFormService.demandMember(productId, requestDto, member);
            });
            assertEquals("이미 수요조사에 참여하였습니다.", e.getMessage());
        }

        @Test
        @DisplayName("실패(일반 유저) - 없는 상품")
        void demandMemberTest_fail_NotFoundProduct() {
            // given
            when(demandFormRepository.findByProductIdAndMemberId(productId, memberId)).thenReturn(Optional.empty());
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // when - then
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
                demandFormService.demandMember(productId, requestDto, member);
            });
            assertEquals("상품을 찾을 수 없습니다.", e.getMessage());
        }

        @Test
        @DisplayName("성공(비회원)")
        void demandNonMemberTest_success() {
            // given
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(demandFormRepository.save(any(DemandForm.class))).thenReturn(nonMemberDemandForm);

            // when
            DemandFormResponseDto responseDto = demandFormService.demandNonMember(productId, requestDto);

            // then
            assertEquals(quantity, responseDto.getQuantity());
            assertEquals(productId, responseDto.getProductId());
            System.out.println("responseDto.orderNumber = " + responseDto.getMemberId());
        }
    }
}

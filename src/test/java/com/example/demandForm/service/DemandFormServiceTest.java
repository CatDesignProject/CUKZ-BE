package com.example.demandForm.service;

import com.example.common.exception.GlobalException;
import com.example.demandForm.DemandTest;
import com.example.demandForm.DemandTestBuilder;
import com.example.demandForm.dto.request.DemandFormRequestDto;
import com.example.demandForm.dto.request.GetFormNonMemberRequestDto;
import com.example.demandForm.dto.response.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.entity.DemandOption;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.demandForm.repository.DemandOptionRepository;
import com.example.member.entity.Member;
import com.example.product.ProductTestBuilder;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.common.exception.BaseErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DemandFormServiceTest implements DemandTest {

    @Mock
    DemandFormRepository demandFormRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    OptionRepository optionRepository;

    @Mock
    DemandOptionRepository demandOptionRepository;

    @InjectMocks
    DemandFormService demandFormService;

    Member member;
    Product product;
    Option option;
    DemandOption demandOption;
    DemandForm memberDemandForm;
    DemandForm nonMemberDemandForm;
    DemandFormRequestDto requestDto;
    Long productId = 1L;
    Long memberId = 1L;
    Long formId = 1L;

    @BeforeEach
    void setUp() {
        member = ProductTestBuilder.testMemberBuild();
        product = ProductTestBuilder.testProductBuild();
        ReflectionTestUtils.setField(product, "id", productId);

        option = DemandTestBuilder.buildOption();
        demandOption = DemandTestBuilder.buildDemandOption(memberDemandForm);
        requestDto = DemandTestBuilder.buildCreateDemandFormRequestDto();
        memberDemandForm = DemandTestBuilder.buildMemberDemandForm(member, product);
        nonMemberDemandForm = DemandTestBuilder.buildNonMemberDemandForm(product);
    }

    @Nested
    @DisplayName("수요조사 참여 테스트")
    class demandTest {

        @Test
        @DisplayName("성공(일반 유저)")
        void demandMemberTest_success() {
            // given
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(demandFormRepository.save(any(DemandForm.class))).thenReturn(memberDemandForm);
            when(optionRepository.findById(any())).thenReturn(Optional.of(option));
            when(demandOptionRepository.save(any(DemandOption.class))).thenReturn(demandOption);

            // when
            DemandFormResponseDto responseDto = demandFormService.demandMember(productId, requestDto, memberId);

            // then
            assertEquals(product.getId(), responseDto.getProductId());
            assertEquals(member.getId(), responseDto.getMemberId());
            assertEquals(requestDto.getOptionList().get(0).getOptionId(),
                    responseDto.getOptionList().get(0).getOptionId());
        }

        @Test
        @DisplayName("실패(일반 유저) - 중복 참여")
        void demandMemberTest_fail_duplicate() {
            // given
            when(demandFormRepository.findByEmailAndProductId(TEST_EMAIL, productId))
                    .thenReturn(Optional.of(nonMemberDemandForm));

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                demandFormService.demandMember(productId, requestDto, memberId);
            });
            assertEquals(DUPLICATED_FORM, e.getErrorCode());
        }

        @Test
        @DisplayName("실패(일반 유저) - 없는 상품")
        void demandMemberTest_fail_NotFoundProduct() {
            // given
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                demandFormService.demandMember(productId, requestDto, memberId);
            });
            assertEquals(NOT_FOUND_PRODUCT, e.getErrorCode());
        }

        @Test
        @DisplayName("실패(일반 유저) - 참여 기간이 아님")
        void demandMemberTest_fail_isNotPeriod() {
            // given
            LocalDateTime endDate = LocalDateTime.of(2000, 4, 1, 12, 0);
            ReflectionTestUtils.setField(product, "endDate", endDate);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                demandFormService.demandMember(productId, requestDto, memberId);
            });
            assertEquals(NOT_IN_PERIOD, e.getErrorCode());
        }

        @Test
        @DisplayName("성공(비회원)")
        void demandNonMemberTest_success() {
            // given
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(demandFormRepository.save(any(DemandForm.class))).thenReturn(memberDemandForm);
            when(optionRepository.findById(any())).thenReturn(Optional.of(option));
            when(demandOptionRepository.save(any(DemandOption.class))).thenReturn(demandOption);

            // when
            DemandFormResponseDto responseDto = demandFormService.demandNonMember(productId, requestDto);

            // then
            assertEquals(productId, responseDto.getProductId());
            assertEquals(requestDto.getOptionList().get(0).getOptionId(),
                    responseDto.getOptionList().get(0).getOptionId());
        }
    }

    @Nested
    @DisplayName("수요조사 참여 폼 조회 테스트")
    class getDemandTest {
        @Test
        @DisplayName("성공(일반 유저) - 단건 조회")
        void getMemberDemandFormTest_success() {
            //given
            when(demandFormRepository.findById(formId)).thenReturn(Optional.of(memberDemandForm));

            //when
            DemandFormResponseDto responseDto = demandFormService.getDemandFormMember(formId, memberId);

            //then
            assertEquals(productId, responseDto.getProductId());
        }

        @Test
        @DisplayName("실패(일반 유저) - 작성자와 불일치")
        void getMemberDemandFormTest_fail_misMatched() {
            //given
            Long memberId2 = 2L;
            when(demandFormRepository.findById(formId)).thenReturn(Optional.of(memberDemandForm));

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                demandFormService.getDemandFormMember(formId, memberId2);
            });
            assertEquals(UNAUTHORIZED_MEMBER, e.getErrorCode());
        }

        @Test
        @DisplayName("성공(일반 유저) - 페이지 조회")
        void getAllDemandFormsMemberTest_success() {
            // given
            int page = 1;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            DemandForm memberDemandForm2 = requestDto.toEntity(memberId, product);
            List<DemandForm> demandFormList = Arrays.asList(memberDemandForm, memberDemandForm2);
            Page<DemandForm> demandFormPage = new PageImpl<>(demandFormList);

            when(demandFormRepository.findByMemberId(memberId, pageable)).thenReturn(demandFormPage);

            // when
            Page<DemandFormResponseDto> responseDtoList = demandFormService.getAllDemandFormsMember(page, size, memberId);

            // then
            assertEquals(demandFormList.size(), responseDtoList.getContent().size());
            assertEquals(demandFormPage.getTotalPages(), responseDtoList.getTotalPages());
            assertEquals(demandFormList.get(0).getId(), responseDtoList.getContent().get(0).getId());
        }

        @Test
        @DisplayName("성공(비회원) - 단건 조회")
        void getNonMemberDemandFormTest_success() {
            //given
            Long orderNumber = 12345L;
            GetFormNonMemberRequestDto requestDto1 = new GetFormNonMemberRequestDto(orderNumber);
            when(demandFormRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(nonMemberDemandForm));
            ReflectionTestUtils.setField(nonMemberDemandForm, "memberId", orderNumber);

            //when
            DemandFormResponseDto responseDto = demandFormService.getDemandFormNonMember(requestDto1);

            //then
            assertEquals(productId, responseDto.getProductId());
            assertEquals(requestDto1.getOrderNumber(), responseDto.getMemberId());
        }

        @Test
        @DisplayName("성공(총대) - 페이지 조회")
        void getAllDemandFormsTest_success() {
            // given
            int page = 1;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            DemandForm memberDemandForm2 = requestDto.toEntity(memberId, product);
            List<DemandForm> demandFormList = Arrays.asList(memberDemandForm, memberDemandForm2);
            Page<DemandForm> demandFormPage = new PageImpl<>(demandFormList, pageable, demandFormList.size());

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(demandFormRepository.findByProductId(eq(productId), any(Pageable.class))).thenReturn(demandFormPage);

            // when
            Page<DemandFormResponseDto> responseDtoList = demandFormService.getAllDemandForms(page, size, productId, memberId);

            // then
            assertEquals(demandFormList.size(), responseDtoList.getContent().size());
            assertEquals(demandFormPage.getTotalPages(), responseDtoList.getTotalPages());
            assertEquals(demandFormList.get(0).getId(), responseDtoList.getContent().get(0).getId());
        }
    }
}

package com.example.likes.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.likes.dto.LikesResponseDto;
import com.example.likes.entity.Likes;
import com.example.likes.repository.LikesRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @Mock
    LikesRepository likesRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    LikesService likesService;

    Member member;
    Product product;
    Likes likes;
    LikesResponseDto responseDto;

    @BeforeEach
    void setUp() {
        member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L);

        product = new Product();
        ReflectionTestUtils.setField(product, "id", 1L);

        likes = Likes.toEntity(member, product);
        responseDto = LikesResponseDto.toResponseDto(product);
    }

    @Nested
    @DisplayName("좋아요 테스트")
    class LikeProductTest {
        @Test
        @DisplayName("좋아요 성공")
        void LikeProductTest_success() {
            //given
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(likesRepository.findByProductIdAndMemberId(any(), any())).thenReturn(Optional.empty());

            //when
            responseDto = likesService.likeProduct(1L, 1L);

            //then
            assertEquals(product.getLikesCount(), responseDto.getLikesCount());
        }

        @Test
        @DisplayName("좋아요 실패 - 존재하지 않는 상품")
        void LikeProductTest_fail_NotfoundProduct() {
            //given
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));
            when(productRepository.findById(any())).thenReturn(Optional.empty());

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                likesService.likeProduct(1L, 1L);
            });
            assertEquals(BaseErrorCode.NOT_FOUND_PRODUCT, e.getErrorCode());
        }

        @Test
        @DisplayName("좋아요 실패 - 이미 좋아요를 누름")
        void LikeProductTest_fail_DuplicatedLikes() {
            //given
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(likesRepository.findByProductIdAndMemberId(any(), any())).thenReturn(Optional.of(likes));

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                likesService.likeProduct(1L, 1L);
            });
            assertEquals(BaseErrorCode.DUPLICATED_LIKES, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("좋아요 취소 테스트")
    class unLikeProductTest {
        @Test
        @DisplayName("좋아요 취소 성공")
        void unLikeProductTest_success() {
            //given
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(likesRepository.findByProductIdAndMemberId(any(), any())).thenReturn(Optional.of(likes));

            //when
            responseDto = likesService.unlikeProduct(1L, 1L);

            //then
            assertEquals(product.getLikesCount(), responseDto.getLikesCount());
        }

        @Test
        @DisplayName("좋아요 취소 실패 - 존재하지 않는 좋아요")
        void unLikeProductTest_fail_NotfoundLikes() {
            //given
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(likesRepository.findByProductIdAndMemberId(any(), any())).thenReturn(Optional.empty());

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                likesService.unlikeProduct(1L, 1L);
            });
            assertEquals(BaseErrorCode.NOT_FOUND_LIKES, e.getErrorCode());
        }
    }
}
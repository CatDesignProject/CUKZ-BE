package com.example.likes.service;

import com.example.common.exception.GlobalException;
import com.example.likes.dto.LikesResponseDto;
import com.example.likes.entity.Likes;
import com.example.likes.repository.LikesRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.common.exception.BaseErrorCode.*;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public LikesResponseDto likeProduct(Long productId, Long memberId) {

        Member member = findMember(memberId);
        Product product = findProduct(productId);
        isAlreadyLiked(productId, memberId);

        Likes likes = Likes.toEntity(member, product);
        likesRepository.save(likes);
        product.updateLikesCount(1);

        return LikesResponseDto.toResponseDto(product.getLikesCount());
    }

    @Transactional
    public LikesResponseDto unlikeProduct(Long productId, Long memberId) {

        Member member = findMember(memberId);
        Product product = findProduct(productId);
        Likes likes = findLikes(productId, memberId);

        likesRepository.delete(likes);
        product.updateLikesCount(-1);

        return LikesResponseDto.toResponseDto(product.getLikesCount());
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_MEMBER));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_PRODUCT));
    }

    private Likes findLikes(Long productId, Long memberId) {
        return likesRepository.findByProductIdAndMemberId(productId, memberId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_LIKES));
    }

    private void isAlreadyLiked(Long productId, Long memberId) {
        likesRepository.findByProductIdAndMemberId(productId, memberId).ifPresent(likes -> {
            throw new GlobalException(DUPLICATED_LIKES);
        });
    }
}

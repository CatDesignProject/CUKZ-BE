package com.example.likes.controller;

import com.example.common.global.PageResponseDto;
import com.example.likes.dto.LikesResponseDto;
import com.example.likes.service.LikesService;
import com.example.product.dto.response.ProductResponseDto;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/products/{productId}/likes")
    public ResponseEntity<LikesResponseDto> likeProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        LikesResponseDto responseDto = likesService.likeProduct(productId, member.getMemberId());

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/products/{productId}/unlikes")
    public ResponseEntity<LikesResponseDto> unlikeProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        LikesResponseDto responseDto = likesService.unlikeProduct(productId, member.getMemberId());

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/members/likes")
    public ResponseEntity<PageResponseDto<ProductResponseDto>> getLikedProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal AuthenticatedMember member) {

        Page<ProductResponseDto> responseDtoPage = likesService.getLikedProducts(page - 1, size, member.getMemberId());

        return ResponseEntity.ok().body(PageResponseDto.toResponseDto(responseDtoPage));
    }
}

package com.example.likes.controller;

import com.example.common.global.BaseResponse;
import com.example.likes.dto.LikesResponseDto;
import com.example.likes.service.LikesService;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> likeProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        LikesResponseDto responseDto = likesService.likeProduct(productId, member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, responseDto));
    }
}

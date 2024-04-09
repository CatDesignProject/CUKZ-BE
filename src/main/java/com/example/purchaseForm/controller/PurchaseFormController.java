package com.example.purchaseForm.controller;

import com.example.common.global.BaseResponse;
import com.example.common.global.PageResponseDto;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import com.example.purchaseForm.dto.PurchaseFormResponseDto;
import com.example.purchaseForm.service.PurchaseFormService;
import com.example.security.authentication.AuthenticatedMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PurchaseFormController {

    private final PurchaseFormService purchaseFormService;

    // 유저
    @PostMapping("/products/{productId}/purchase/members")
    public ResponseEntity<BaseResponse<PurchaseFormResponseDto>> purchaseMember(
            @PathVariable Long productId,
            @Valid @RequestBody PurchaseFormRequestDto requestDto,
            @AuthenticationPrincipal AuthenticatedMember member) {

        PurchaseFormResponseDto responseDto = purchaseFormService.purchaseMember(productId, requestDto, member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, responseDto));
    }

    @PostMapping("/products/{productId}/purchase/non-members")
    public ResponseEntity<BaseResponse<PurchaseFormResponseDto>> purchaseNonMember(
            @PathVariable Long productId,
            @Valid @RequestBody PurchaseFormRequestDto requestDto) {

        PurchaseFormResponseDto responseDto = purchaseFormService.purchaseNonMember(productId, requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, responseDto));
    }

    @GetMapping("/members/purchase/{purchaseFormId}")
    public ResponseEntity<BaseResponse<PurchaseFormResponseDto>> getPurchaseFormMember(
            @PathVariable Long purchaseFormId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        PurchaseFormResponseDto responseDto = purchaseFormService.getPurchaseFormMember(purchaseFormId, member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, responseDto));
    }

    @GetMapping("/members/purchase")
    public ResponseEntity<BaseResponse<PageResponseDto<PurchaseFormResponseDto>>> getAllPurchaseFormsMember(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal AuthenticatedMember member) {

        Page<PurchaseFormResponseDto> responseDtoList = purchaseFormService.getAllPurchaseFormsMember(page - 1, size,
                member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, PageResponseDto.toResponseDto(responseDtoList)));
    }
}

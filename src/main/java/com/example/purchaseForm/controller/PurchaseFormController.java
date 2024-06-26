package com.example.purchaseForm.controller;

import com.example.common.global.PageResponseDto;
import com.example.demandForm.dto.request.GetFormNonMemberRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.purchaseForm.dto.PayRequestDto;
import com.example.purchaseForm.dto.ProductPurchaseRequestDto;
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
    public ResponseEntity<PurchaseFormResponseDto> purchaseMember(
            @PathVariable Long productId,
            @Valid @RequestBody PurchaseFormRequestDto requestDto,
            @AuthenticationPrincipal AuthenticatedMember member) {

        PurchaseFormResponseDto responseDto = purchaseFormService.purchaseMember(productId, requestDto, member.getMemberId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/products/{productId}/purchase/non-members")
    public ResponseEntity<PurchaseFormResponseDto> purchaseNonMember(
            @PathVariable Long productId,
            @Valid @RequestBody PurchaseFormRequestDto requestDto) {

        PurchaseFormResponseDto responseDto = purchaseFormService.purchaseNonMember(productId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/members/purchase")
    public ResponseEntity<PageResponseDto<PurchaseFormResponseDto>> getAllPurchaseFormsMember(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal AuthenticatedMember member) {

        Page<PurchaseFormResponseDto> responseDtoList = purchaseFormService.getAllPurchaseFormsMember(page - 1, size,
                member.getMemberId());

        return ResponseEntity.ok().body(PageResponseDto.toResponseDto(responseDtoList));
    }

    @GetMapping("/members/purchase/{purchaseFormId}")
    public ResponseEntity<PurchaseFormResponseDto> getPurchaseFormMember(
            @PathVariable Long purchaseFormId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        PurchaseFormResponseDto responseDto = purchaseFormService.getPurchaseFormMember(purchaseFormId, member.getMemberId());

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/purchase/non-members")
    public ResponseEntity<PurchaseFormResponseDto> getPurchaseFormNonMember(
            @RequestBody GetFormNonMemberRequestDto requestDto) {

        PurchaseFormResponseDto responseDto = purchaseFormService.getPurchaseFormNonMember(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // 관리자
    @DeleteMapping("/purchase/{purchaseFormId}")
    public ResponseEntity<String> deletePurchaseForm(
            @PathVariable Long purchaseFormId) {

        purchaseFormService.deletePurchaseForm(purchaseFormId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("구매 내역이 삭제되었습니다.");
    }

    // 총대
    @GetMapping("/products/{productId}/purchase")
    public ResponseEntity<PageResponseDto<PurchaseFormResponseDto>> getAllPurchaseForms(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        Page<PurchaseFormResponseDto> responseDtoList = purchaseFormService.getAllPurchaseForms(page - 1, size, productId,
                member.getMemberId());

        return ResponseEntity.ok().body(PageResponseDto.toResponseDto(responseDtoList));
    }

    @PatchMapping("products/{productId}/purchase")
    public ResponseEntity<ProductResponseDto> modifyPurchaseForm(
            @PathVariable Long productId,
            @RequestBody ProductPurchaseRequestDto requestDto,
            @AuthenticationPrincipal AuthenticatedMember member) {

        ProductResponseDto responseDto = purchaseFormService.modifyPurchaseForm(productId, requestDto, member.getMemberId());

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/purchase/{purchaseFormId}/pay")
    public ResponseEntity<String> updatePayStatus(
            @PathVariable Long purchaseFormId,
            @Valid @RequestBody PayRequestDto requestDto,
            @AuthenticationPrincipal AuthenticatedMember member) {

        purchaseFormService.updatePayStatus(purchaseFormId, requestDto, member.getMemberId());

        return ResponseEntity.ok().body("입금 상태 변경이 완료되었습니다.");
    }
}

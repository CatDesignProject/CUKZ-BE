package com.example.product.controller;

import com.example.common.global.BaseResponse;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.service.ProductService;
import com.example.security.authentication.AuthenticatedMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<BaseResponse<String>> saveProduct(@Valid @RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        Long resultId = productService.saveProduct(productRequestDto, authenticatedMember.getMemberId());
        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, resultId + "번 상품 등록 완료"));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse<ProductResponseDto>> findProduct(@PathVariable Long productId) {
        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, productService.findProduct(productId)));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<BaseResponse<String>> modifyProduct(@PathVariable Long productId, @RequestBody ProductRequestDto productRequestDto) {
        productService.modifyProduct(productId, productRequestDto);
        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, "수정 완료"));
    }
}

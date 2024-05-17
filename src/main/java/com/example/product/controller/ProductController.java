package com.example.product.controller;

import com.example.common.global.PageResponseDto;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.dto.response.ProductThumbNailDto;
import com.example.product.service.ProductService;
import com.example.security.authentication.AuthenticatedMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@Valid @RequestBody ProductRequestDto productRequestDto
            , @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        return ResponseEntity.ok().body(productService.saveProduct(productRequestDto, authenticatedMember.getMemberId()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> findProduct(@PathVariable Long productId
            , @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        ProductResponseDto responseDto;
        if(authenticatedMember == null) {
            responseDto = productService.findProduct(productId, null);
        } else {
            responseDto = productService.findProduct(productId, authenticatedMember.getMemberId());
        }
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> modifyProduct(@PathVariable Long productId
            , @RequestBody ProductRequestDto productRequestDto
            , @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        return ResponseEntity.ok().body(productService.modifyProduct(productId, productRequestDto, authenticatedMember.getMemberId()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId
            , @AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        productService.deleteProduct(productId, authenticatedMember.getMemberId());
        return ResponseEntity.ok().body("상품 삭제 완료");
    }

    @GetMapping("/paging")
    public ResponseEntity<PageResponseDto<ProductThumbNailDto>> pagingProductThumbNail(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.pagingProduct(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<ProductThumbNailDto>> searchProduct(@RequestParam String keyword
            , @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.searchProduct(keyword, pageable));
    }
}
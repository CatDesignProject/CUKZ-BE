package com.example.product.controller;

import com.example.product.dto.request.ProductRequestDto;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> saveProduct(@RequestBody ProductRequestDto productRequestDto) {
        Long resultId = productService.saveProduct(productRequestDto);

        return ResponseEntity.ok("상품 ID : " + resultId + "번이 등록 되었습니다.");
    }
}

package com.example.product_image.controller;

import com.example.common.global.BaseResponse;
import com.example.product_image.dto.response.ProductImageResponse;
import com.example.product_image.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/products/image-upload")
    public ResponseEntity<BaseResponse<List<ProductImageResponse>>> productImageUpload(@RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, productImageService.fileUpload(files)));
    }

}

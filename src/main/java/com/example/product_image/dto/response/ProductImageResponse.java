package com.example.product_image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageResponse {

    private String imageUrl; //실제 이미지 url
    private Long productImageId; //ProductImage pk값, Product 등록 컨트롤러에서 받아서 연관관계(Product엔티티와, ProductImage) 맺을 때 사용
}

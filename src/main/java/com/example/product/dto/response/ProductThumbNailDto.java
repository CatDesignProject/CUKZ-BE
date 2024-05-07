package com.example.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductThumbNailDto {

    private Long id;
    private String productName; //상품명
    private int price;
    private int likesCount;
    private String imageUrl;
    private String nickname; //총대명
}

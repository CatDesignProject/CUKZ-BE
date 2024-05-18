package com.example.product.dto.response;

import com.example.product.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private SaleStatus saleStatus;
}

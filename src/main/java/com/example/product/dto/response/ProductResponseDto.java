package com.example.product.dto.response;

import com.example.product.dto.ProductOption;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponseDto {

    private SaleStatus status;
    private String name;
    private int price;
    private String info;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> imageUrls;
    private String nickname; //총대 닉네임
    private int likesCount;
    private List<ProductOption> options;

    public static ProductResponseDto toResponseDto(Product product, List<ProductOption> productOptions) {
        List<ProductImage> productImages = product.getProductImages();

        List<String> imageUrls = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            imageUrls.add(productImage.getImageUrl());
        }

        return ProductResponseDto.builder()
                .status(product.getStatus())
                .name(product.getName())
                .price(product.getPrice())
                .info(product.getInfo())
                .startDate(product.getStartDate())
                .endDate(product.getEndDate())
                .imageUrls(imageUrls)
                .nickname(product.getMember().getNickname())
                .likesCount(product.getLikesCount())
                .options(productOptions)
                .build();
    }
}


package com.example.product.dto.response;

import com.example.product.dto.ProductOptionDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Long id;
    private SaleStatus status;
    private String name;
    private int price;
    private String info;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> imageUrls;
    private String nickname; //총대 닉네임
    private int likesCount;
    private List<ProductOptionDto> options;
    @JsonProperty("isLiked")
    private Boolean isLiked;

    public static ProductResponseDto toResponseDto(Product product) {
        List<ProductImage> productImages = product.getProductImages();
        List<String> imageUrls = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            imageUrls.add(productImage.getImageUrl());
        }

        List<Option> optionList = product.getOptions();
        List<ProductOptionDto> productOptionDtos = new ArrayList<>();
        for (Option option : optionList) {
            productOptionDtos.add(ProductOptionDto.toProductOptionDto(option));
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .status(product.getStatus())
                .name(product.getName())
                .price(product.getPrice())
                .info(product.getInfo())
                .startDate(product.getStartDate())
                .endDate(product.getEndDate())
                .imageUrls(imageUrls)
                .nickname(product.getMember().getNickname())
                .likesCount(product.getLikesCount())
                .options(productOptionDtos)
                .build();
    }

    public static ProductResponseDto toResponseDto(Product product, boolean isLiked) {
        List<ProductImage> productImages = product.getProductImages();
        List<String> imageUrls = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            imageUrls.add(productImage.getImageUrl());
        }

        List<Option> optionList = product.getOptions();
        List<ProductOptionDto> productOptionDtos = new ArrayList<>();
        for (Option option : optionList) {
            productOptionDtos.add(ProductOptionDto.toProductOptionDto(option));
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
                .options(productOptionDtos)
                .isLiked(isLiked)
                .build();
    }
}
package com.example.likes.dto;

import com.example.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikesResponseDto {

    private Long productId;
    private int likesCount;

    public static LikesResponseDto toResponseDto(Product product) {
        return LikesResponseDto.builder()
                .productId(product.getId())
                .likesCount(product.getLikesCount())
                .build();
    }
}

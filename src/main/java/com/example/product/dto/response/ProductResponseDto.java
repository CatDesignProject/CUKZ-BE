package com.example.product.dto.response;

import com.example.product.entity.Goods;
import com.example.product.entity.Jacket;
import com.example.product.entity.Product;
import com.example.product.enums.ProductType;
import com.example.product.enums.Size;
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

    private String name;
    private int price;
    private String info;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> colors;
    private List<Size> sizes;
    private List<String> imageUrls;
    private String nickname; //총대 닉네임

    public static ProductResponseDto toResponseDto(Product product) {
        if (product.getType() == ProductType.잠바) {
            Jacket jacket = (Jacket) product;

            List<ProductImage> productImages = jacket.getProductImages();
            List<String> imageUrls = new ArrayList<>();

            addToImageUrls(productImages, imageUrls);

            return ProductResponseDto.builder()
                    .name(jacket.getName())
                    .price(jacket.getPrice())
                    .info(jacket.getInfo())
                    .startDate(jacket.getStartDate())
                    .endDate(jacket.getEndDate())
                    .colors(jacket.getColors())
                    .sizes(jacket.getAvailableSizes())
                    .imageUrls(imageUrls)
                    .nickname(product.getMember().getNickname())
                    .build();
        } else {
            Goods goods = (Goods) product;

            List<ProductImage> productImages = goods.getProductImages();
            List<String> imageUrls = new ArrayList<>();

            addToImageUrls(productImages, imageUrls);

            return ProductResponseDto.builder()
                    .name(goods.getName())
                    .price(goods.getPrice())
                    .info(goods.getInfo())
                    .startDate(goods.getStartDate())
                    .endDate(goods.getEndDate())
                    .colors(goods.getColors())
                    .imageUrls(imageUrls)
                    .nickname(product.getMember().getNickname())
                    .build();
        }
    }

    private static void addToImageUrls (List<ProductImage> productImages, List<String> imageUrls) {
        for (ProductImage productImage : productImages) {
            String imageUrl = productImage.getImageUrl();
            imageUrls.add(imageUrl);
        }
    }
}


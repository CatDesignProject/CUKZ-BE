package com.example.product;

import com.example.member.entity.Member;
import com.example.member.entity.MemberRole;
import com.example.product.dto.ProductOption;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestBuilder {
    public static ProductRequestDto testProductRequestDtoBuild() {
        ProductOption option1 = new ProductOption("블랙 S", 3000, 100);
        ProductOption option2 = new ProductOption("화이트 M", 0, 200);

        return ProductRequestDto.builder()
                .status(SaleStatus.ON_DEMAND)
                .name("가톨릭대학교 학잠")
                .price(50000)
                .info("2024학년도 가톨릭대학교 학잠")
                .startDate(LocalDateTime.of(2024, 3, 18, 15, 30, 0))
                .endDate(LocalDateTime.of(2024, 3, 30, 15, 30, 0))
                .productImageIds(Arrays.asList(1L, 2L))
                .options(Arrays.asList(option1, option2))
                .build();
    }

    public static Product testProductBuild() {
        ProductRequestDto requestDto = TestBuilder.testProductRequestDtoBuild();
        Member member = TestBuilder.testMemberBuild();
        List<ProductOption> productOptions = requestDto.getOptions();
        List<Option> options = new ArrayList<>();
        for (ProductOption option : productOptions) {
            Option toEntity = option.toOption();
            options.add(toEntity);
        }

        List<ProductImage> productImages = new ArrayList<>();

        ProductImage productImage1 = ProductImage.builder()
                .imageUrl("www.s3v1.png")
                .id(1L)
                .uploadFileName("A")
                .storeFileName("B")
                .build();

        ProductImage productImage2 = ProductImage.builder()
                .imageUrl("www.s3v2.png")
                .id(2L)
                .uploadFileName("C")
                .storeFileName("D")
                .build();

        productImages.add(productImage1);
        productImages.add(productImage2);

        Product product = new Product(1L, requestDto.getName(), requestDto.getPrice(), requestDto.getInfo(), requestDto.getStatus()
        ,requestDto.getStartDate(), requestDto.getEndDate(), 0, member, productImages, options);

        return product;
    }

    public static Member testMemberBuild() {
        return new Member(1L, "username1", "password1234!", "nickname1"
                , "email@naver.com", MemberRole.manager);
    }

}

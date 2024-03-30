package com.example.product;

import com.example.member.entity.Member;
import com.example.member.entity.MemberRole;
import com.example.product.dto.ProductOption;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

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
        return TestBuilder.testProductRequestDtoBuild().toProduct();
    }

    public static Member testMemberBuild() {
        return new Member(1L, "username1", "password1234!", "nickname1"
                , "email@naver.com", MemberRole.manager);
    }

}

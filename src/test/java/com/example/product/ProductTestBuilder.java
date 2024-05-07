package com.example.product;

import com.example.member.entity.Member;
import com.example.member.entity.MemberRole;
import com.example.product.dto.ProductOptionDto;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import com.example.purchaseForm.entity.Delivery;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductTestBuilder {
    public static ProductRequestDto testProductRequestDtoBuild() {
        ProductOptionDto option1 = new ProductOptionDto("블랙 S", 3000);
        ProductOptionDto option2 = new ProductOptionDto("화이트 M", 0);

        return ProductRequestDto.builder()
                .status(SaleStatus.ON_DEMAND)
                .name("가톨릭대학교 학잠")
                .price(50000)
                .info("2024학년도 가톨릭대학교 학잠")
                .startDate(LocalDateTime.of(2024, 3, 18, 15, 30, 0))
                .endDate(LocalDateTime.of(3024, 3, 30, 15, 30, 0))
                .productImageIds(Arrays.asList(1L, 2L))
                .options(Arrays.asList(option1, option2))
                .build();
    }

    public static ProductRequestDto testProductRequestDto2Build() {
        ProductOptionDto option1 = new ProductOptionDto("블랙 L", 3000);
        ProductOptionDto option2 = new ProductOptionDto("화이트 XL", 0);

        return ProductRequestDto.builder()
                .status(SaleStatus.ON_DEMAND)
                .name("가톨릭대학교 컴공 과잠")
                .price(50000)
                .info("2024학년도 가톨릭대학교 학잠")
                .startDate(LocalDateTime.of(2024, 3, 18, 15, 30, 0))
                .endDate(LocalDateTime.of(3024, 3, 30, 15, 30, 0))
                .productImageIds(Arrays.asList(3L, 4L))
                .options(Arrays.asList(option1, option2))
                .build();
    }

    public static Member testMemberBuild() {
        return new Member(1L, "username1", "password1234!", "nickname1"
                , "email@naver.com", MemberRole.manager);
    }

    public static Member testMember2Build() {
        return new Member(2L, "username2", "password1234!!", "nickname2"
                , "email2@naver.com", MemberRole.manager);
    }

    public static Product testProductBuild() {
        ProductRequestDto requestDto = testProductRequestDtoBuild();
        Member member = testMemberBuild();
        List<ProductOptionDto> productOptions = requestDto.getOptions();
        List<Option> options = new ArrayList<>();
        List<Delivery> deliveries = new ArrayList<>();

        for (ProductOptionDto productOption : productOptions) {
            Option option = productOption.toOption();
            options.add(option);
        }

        List<ProductImage> productImages = new ArrayList<>();
        ProductImage productImage1 = ProductImage.builder()
                .id(1L)
                .imageUrl("www.s3v1.png")
                .uploadFileName("A")
                .storeFileName("B")
                .build();
        ProductImage productImage2 = ProductImage.builder()
                .id(2L)
                .imageUrl("www.s3v2.png")
                .uploadFileName("C")
                .storeFileName("D")
                .build();
        productImages.add(productImage1);
        productImages.add(productImage2);

        Product product = new Product(1L, requestDto.getName(), requestDto.getPrice(), requestDto.getInfo(), requestDto.getStatus()
                , requestDto.getStartDate(), requestDto.getEndDate(), member, 0, productImages, options);

        return product;
    }

    public static Product testProduct2Build() {
        ProductRequestDto requestDto = testProductRequestDto2Build();
        Member member = testMember2Build();
        List<ProductOptionDto> productOptions = requestDto.getOptions();
        List<Option> options = new ArrayList<>();

        for (ProductOptionDto productOption : productOptions) {
            Option option = productOption.toOption();
            options.add(option);
        }

        List<ProductImage> productImages = new ArrayList<>();
        ProductImage productImage3 = ProductImage.builder()
                .id(3L)
                .imageUrl("www.s3v3.png")
                .uploadFileName("E")
                .storeFileName("F")
                .build();
        ProductImage productImage4 = ProductImage.builder()
                .id(4L)
                .imageUrl("www.s3v4.png")
                .uploadFileName("G")
                .storeFileName("H")
                .build();
        productImages.add(productImage3);
        productImages.add(productImage4);

        Product product = new Product(2L, requestDto.getName(), requestDto.getPrice(), requestDto.getInfo(), requestDto.getStatus()
                , requestDto.getStartDate(), requestDto.getEndDate(), member, 0, productImages, options);

        return product;
    }

    public static Pageable testPageableBuild() {
        return PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
    }

    public static Page<Product> testPageProductBuild() {
        Pageable pageable = testPageableBuild();
        Product product1 = testProductBuild();
        Product product2 = testProduct2Build();
        List<Product> products = Arrays.asList(product1, product2);
        return new PageImpl<>(products, pageable, products.size());
    }
}

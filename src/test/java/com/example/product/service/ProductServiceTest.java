package com.example.product.service;

import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.entity.Goods;
import com.example.product.entity.Jacket;
import com.example.product.entity.Product;
import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import com.example.product.enums.Size;
import com.example.product.repository.ProductRepository;
import com.example.product_image.entity.ProductImage;
import com.example.product_image.repository.ProductImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductImageRepository productImageRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    ProductService productService;

    LocalDateTime startDate = LocalDateTime.of(2024, 3, 18, 15, 30, 0);
    LocalDateTime endDate = LocalDateTime.of(2024, 3, 19, 15, 30, 0);

    @DisplayName("상품 단건 조회(잠바)")
    @Test
    void find_jacket() {
        //given
        Long productId = 1L;
        List<String> colors = new ArrayList<>();
        colors.add("NAVY");

        List<Size> sizes = new ArrayList<>();
        sizes.add(Size.S);

        Jacket jacket = new Jacket();
        jacket.modify("A", 1, "B", ProductType.잠바, SaleStatus.ON_SALE, startDate, endDate, colors, sizes);

        Product product = jacket;
        Member member = new Member();
        member.changeNickname("닉네임");
        product.addMember(member);
        ProductImage productImage = ProductImage.builder()
                .id(1L)
                .product(product)
                .storeFileName("A")
                .uploadFileName("B")
                .imageUrl("www")
                .build();

        product.addProductImage(productImage);

        given(productRepository.findFetchById(productId)).willReturn(Optional.of(product));

        //when
        ProductResponseDto responseDto = productService.findProduct(productId);

        //then
        Jacket jacket1 = (Jacket) product;
        assertEquals(jacket1.getName(), responseDto.getName());
        assertEquals(jacket1.getPrice(), responseDto.getPrice());
        assertEquals(jacket1.getInfo(), responseDto.getInfo());
        assertEquals(jacket1.getStartDate(), responseDto.getStartDate());
        assertEquals(jacket1.getEndDate(), responseDto.getEndDate());
        assertEquals(jacket1.getColors(), responseDto.getColors());
        assertEquals(jacket1.getMember().getNickname(), responseDto.getNickname());
    }

    @DisplayName("상품 단건 조회(굿즈)")
    @Test
    void find_goods() {
        //given
        Long productId = 1L;
        List<String> colors = new ArrayList<>();
        colors.add("NAVY");

        Goods goods = new Goods();
        goods.modify("A", 1, "B", ProductType.굿즈, SaleStatus.ON_SALE, startDate, endDate, colors);

        Product product = goods;
        Member member = new Member();
        member.changeNickname("닉네임");
        product.addMember(member);
        ProductImage productImage = ProductImage.builder()
                .id(1L)
                .product(product)
                .storeFileName("A")
                .uploadFileName("B")
                .imageUrl("www")
                .build();

        product.addProductImage(productImage);

        given(productRepository.findFetchById(productId)).willReturn(Optional.of(product));

        //when
        ProductResponseDto responseDto = productService.findProduct(productId);

        //then
        Goods goods1 = (Goods) product;
        assertEquals(goods1.getName(), responseDto.getName());
        assertEquals(goods1.getPrice(), responseDto.getPrice());
        assertEquals(goods1.getInfo(), responseDto.getInfo());
        assertEquals(goods1.getStartDate(), responseDto.getStartDate());
        assertEquals(goods1.getEndDate(), responseDto.getEndDate());
        assertEquals(goods1.getColors(), responseDto.getColors());
        assertEquals(goods1.getMember().getNickname(), responseDto.getNickname());
    }
}
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

    @DisplayName("상품 수정(잠바)")
    @Test
    void modify_jacket() {
        //given
        Long productId = 1L;
        Product product = new Jacket();
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        List<String> colors = new ArrayList<>();
        colors.add("NAVY");

        List<Size> sizes = new ArrayList<>();
        sizes.add(Size.S);
        sizes.add(Size.L);

        ProductRequestDto requestDto = ProductRequestDto.builder()
                .name("컴공 과잠")
                .price(50000)
                .info("가톨릭대학교 컴퓨터정보공학부 19학번 과잠입니다.")
                .type(ProductType.잠바)
                .status(SaleStatus.ON_SALE)
                .startDate(startDate)
                .endDate(endDate)
                .colors(colors)
                .sizes(sizes)
                .build();

        //when
        productService.modifyProduct(productId, requestDto);

        //then
        Jacket jacket = (Jacket) product;
        assertEquals("컴공 과잠", jacket.getName());
        assertEquals(50000, jacket.getPrice());
        assertEquals("가톨릭대학교 컴퓨터정보공학부 19학번 과잠입니다.", jacket.getInfo());
        assertEquals(ProductType.잠바, jacket.getType());
        assertEquals(SaleStatus.ON_SALE, jacket.getStatus());
        assertEquals(startDate, jacket.getStartDate());
        assertEquals(endDate, jacket.getEndDate());
        assertEquals(colors, jacket.getColors());
        assertEquals(sizes, jacket.getAvailableSizes());
    }

    @DisplayName("상품 수정(굿즈)")
    @Test
    void modify_goods() {
        //given
        Long productId = 1L;
        Product product = new Goods();
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        List<String> colors = new ArrayList<>();
        colors.add("YELLOW");
        colors.add("WHITE");

        ProductRequestDto requestDto = ProductRequestDto.builder()
                .name("치삼이 키링")
                .price(10000)
                .info("치삼이 키링 공구 진행합니다.")
                .type(ProductType.굿즈)
                .status(SaleStatus.ON_SALE)
                .startDate(startDate)
                .endDate(endDate)
                .colors(colors)
                .build();

        //when
        productService.modifyProduct(productId, requestDto);

        //then
        Goods goods = (Goods) product;
        assertEquals("치삼이 키링", goods.getName());
        assertEquals(10000, goods.getPrice());
        assertEquals("치삼이 키링 공구 진행합니다.", goods.getInfo());
        assertEquals(ProductType.굿즈, goods.getType());
        assertEquals(SaleStatus.ON_SALE, goods.getStatus());
        assertEquals(startDate, goods.getStartDate());
        assertEquals(endDate, goods.getEndDate());
        assertEquals(colors, goods.getColors());
    }
}
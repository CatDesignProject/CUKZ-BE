package com.example.product.service;

import com.example.common.exception.GlobalException;
import com.example.member.entity.Member;
import com.example.member.entity.MemberRole;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

    @Nested
    @DisplayName("상품 등록(성공)")
    class SaveProductSuccess {
        @DisplayName("잠바")
        @Test
        void save_jacket() {
            //given
            List<String> colors = new ArrayList<>();
            colors.add("NAVY");
            List<Size> sizes = new ArrayList<>();
            sizes.add(Size.S);
            List<Long> imageUrls = new ArrayList<>();
            imageUrls.add(1L);

            ProductRequestDto requestDto = ProductRequestDto.builder()
                    .name("A")
                    .price(1)
                    .info("B")
                    .startDate(startDate)
                    .endDate(endDate)
                    .colors(colors)
                    .sizes(sizes)
                    .type(ProductType.잠바)
                    .status(SaleStatus.ON_SALE)
                    .productImageIds(imageUrls)
                    .build();

            Member member = new Member();
            member.changeNickname("닉네임");

            Jacket jacket = requestDto.toJacket();
            jacket.addMember(member);

            ProductImage productImage = ProductImage.builder()
                    .id(1L)
                    .storeFileName("A")
                    .uploadFileName("B")
                    .imageUrl("www")
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(member));
            given(productImageRepository.findById(1L)).willReturn(Optional.of(productImage));
            given(productRepository.save(any(Product.class))).willReturn(jacket);


            //when
            ProductResponseDto result = productService.saveProduct(requestDto, 1L);

            //then
            assertEquals(requestDto.getName(), result.getName());
            assertEquals(requestDto.getPrice(), result.getPrice());
            assertEquals(requestDto.getInfo(), result.getInfo());
            assertEquals(requestDto.getSizes(), result.getSizes());
            assertEquals(requestDto.getColors(), result.getColors());
            assertEquals(requestDto.getStartDate(), result.getStartDate());
            assertEquals(requestDto.getEndDate(), result.getEndDate());
        }

        @DisplayName("굿즈")
        @Test
        void save_goods() {
            //given
            List<String> colors = new ArrayList<>();
            colors.add("NAVY");
            List<Size> sizes = new ArrayList<>();
            sizes.add(Size.S);
            List<Long> imageUrls = new ArrayList<>();
            imageUrls.add(1L);

            Member member = new Member();
            member.changeNickname("닉네임");

            ProductRequestDto requestDto = ProductRequestDto.builder()
                    .name("A")
                    .price(1)
                    .info("B")
                    .startDate(startDate)
                    .endDate(endDate)
                    .colors(colors)
                    .type(ProductType.굿즈)
                    .status(SaleStatus.ON_SALE)
                    .productImageIds(imageUrls)
                    .build();

            Goods goods = requestDto.toGoods();
            goods.addMember(member);

            ProductImage productImage = ProductImage.builder()
                    .id(1L)
                    .storeFileName("A")
                    .uploadFileName("B")
                    .imageUrl("www")
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(member));
            given(productImageRepository.findById(1L)).willReturn(Optional.of(productImage));
            given(productRepository.save(any(Product.class))).willReturn(goods);

            //when
            ProductResponseDto result = productService.saveProduct(requestDto, 1L);

            //then
            assertEquals(requestDto.getName(), result.getName());
            assertEquals(requestDto.getPrice(), result.getPrice());
            assertEquals(requestDto.getInfo(), result.getInfo());
            assertEquals(requestDto.getColors(), result.getColors());
            assertEquals(requestDto.getStartDate(), result.getStartDate());
            assertEquals(requestDto.getEndDate(), result.getEndDate());
        }
    }

    @Nested
    @DisplayName("상품 등록(실패)")
    class SaveProductFail {
        @DisplayName("해당 유저를 찾지 못한 경우")
        @Test
        void not_found_user() {
            //given
            ProductRequestDto requestDto = ProductRequestDto.builder()
                    .name("A")
                    .price(1)
                    .info("B")
                    .startDate(startDate)
                    .endDate(endDate)
                    .type(ProductType.굿즈)
                    .status(SaleStatus.ON_SALE)
                    .build();
            given(memberRepository.findById(1L)).willReturn(Optional.empty());

            //when-then
            assertThatThrownBy(() -> productService.saveProduct(requestDto, 1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("아이디가 일치하지 않습니다.");
        }

        @DisplayName("해당 이미지를 찾지 못한 경우")
        @Test
        void not_found_image() {
            //given
            List<Long> imageUrls = new ArrayList<>();
            imageUrls.add(1L);

            ProductRequestDto requestDto = ProductRequestDto.builder()
                    .name("A")
                    .price(1)
                    .info("B")
                    .startDate(startDate)
                    .endDate(endDate)
                    .type(ProductType.굿즈)
                    .status(SaleStatus.ON_SALE)
                    .productImageIds(imageUrls)
                    .build();

            Member member = new Member();
            Goods goods = requestDto.toGoods();
            goods.addMember(member);

            given(memberRepository.findById(1L)).willReturn(Optional.of(member));
            given(productImageRepository.findById(1L)).willReturn(Optional.empty());

            //when-then
            assertThatThrownBy(() -> productService.saveProduct(requestDto, 1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("해당 이미지를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("상품 단건 조회(성공)")
    class FindProductSuccess {
        @DisplayName("잠바")
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

        @DisplayName("굿즈")
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

    @Nested
    @DisplayName("상품 단건 조회(실패)")
    class FindProductFail {
        @DisplayName("상품을 찾지 못한 경우")
        @Test
        void not_found_product() {
            //given
            given(productRepository.findFetchById(1L)).willReturn(Optional.empty());

            //when-then
            assertThatThrownBy(() -> productService.findProduct(1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("해당 상품을 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("상품 수정(성공)")
    class ModifyProductSuccess {
        @DisplayName("잠바")
        @Test
        void modify_jacket() {
            //given
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

            Member member = new Member(1L, "username", "password1234@"
                    , "닉네임", "email@naver.com", MemberRole.manager);

            Jacket jacket = requestDto.toJacket();
            jacket.addMember(member);

            given(productRepository.findById(1L)).willReturn(Optional.of(jacket));


            //when
            ProductResponseDto result = productService.modifyProduct(1L, requestDto, 1L);

            //then
            assertEquals(requestDto.getName(), result.getName());
            assertEquals(requestDto.getPrice(), result.getPrice());
            assertEquals(requestDto.getInfo(), result.getInfo());
            assertEquals(requestDto.getSizes(), result.getSizes());
            assertEquals(requestDto.getColors(), result.getColors());
            assertEquals(requestDto.getStartDate(), result.getStartDate());
            assertEquals(requestDto.getEndDate(), result.getEndDate());
        }

        @DisplayName("굿즈")
        @Test
        void modify_goods() {
            //given
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

            Member member = new Member(1L, "username", "password1234@"
                    , "닉네임", "email@naver.com", MemberRole.manager);

            Goods goods = requestDto.toGoods();
            goods.addMember(member);

            given(productRepository.findById(1L)).willReturn(Optional.of(goods));

            //when
            ProductResponseDto result = productService.modifyProduct(1L, requestDto, 1L);

            //then
            assertEquals(requestDto.getName(), result.getName());
            assertEquals(requestDto.getPrice(), result.getPrice());
            assertEquals(requestDto.getInfo(), result.getInfo());
            assertEquals(requestDto.getColors(), result.getColors());
            assertEquals(requestDto.getStartDate(), result.getStartDate());
            assertEquals(requestDto.getEndDate(), result.getEndDate());
        }
    }

    @Nested
    @DisplayName("상품 수정(실패)")
    class ModifyProductFail {
        @DisplayName("수정할 대상 상품을 찾지 못한 경우")
        @Test
        void not_found_product() {
            //given
            ProductRequestDto requestDto = ProductRequestDto.builder()
                    .name("치삼이 키링")
                    .price(10000)
                    .info("치삼이 키링 공구 진행합니다.")
                    .type(ProductType.굿즈)
                    .status(SaleStatus.ON_SALE)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            given(productRepository.findById(1L)).willReturn(Optional.empty());

            //when-then
            assertThatThrownBy(() -> productService.modifyProduct(1L, requestDto, 1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("해당 상품을 찾을 수 없습니다.");
        }

        @DisplayName("상품을 등록한 자가 아닌 경우")
        @Test
        void unauthorized_modify_product() {
            //given
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

            Member member = new Member(1L, "username", "password1234@"
                    , "닉네임", "email@naver.com", MemberRole.manager);

            Jacket jacket = requestDto.toJacket();
            jacket.addMember(member);

            given(productRepository.findById(1L)).willReturn(Optional.of(jacket));

            //when-then
            assertThatThrownBy(() -> productService.modifyProduct(1L, requestDto, 2L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("해당 상품을 수정할 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("상품 삭제")
    class DeleteProduct {
        @DisplayName("성공")
        @Test
        void delete_product_success() {
            //given
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

            Member member = new Member(1L, "username", "password1234@"
                    , "닉네임", "email@naver.com", MemberRole.manager);

            Jacket jacket = requestDto.toJacket();
            jacket.addMember(member);
            Long productId = 1L;

            given(productRepository.findById(productId)).willReturn(Optional.of(jacket));
            doNothing().when(productImageRepository).deleteAllByProductId(productId);
            doNothing().when(productRepository).deleteById(productId);

            //when
            productService.deleteProduct(productId, member.getId());

            //then
            then(productRepository).should(times(1)).findById(productId);
            then(productImageRepository).should(times(1)).deleteAllByProductId(productId);
            then(productRepository).should(times(1)).deleteById(productId);
        }

        @DisplayName("실패 - 삭제할 상품이 존재하지 않는 경우")
        @Test
        void not_found_product() {
            //given
            given(productRepository.findById(1L)).willReturn(Optional.empty());

            //when-then
            assertThatThrownBy(() -> productService.deleteProduct(1L, 1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("해당 상품을 찾을 수 없습니다.");
        }

        @DisplayName("실패 - 삭제 권한 없는 경우")
        @Test
        void unauthorized_delete_product() {
            //given
            Product product = new Product();
            Member member = new Member(1L, "username", "password1234@"
                    , "닉네임", "email@naver.com", MemberRole.manager);
            product.addMember(member);
            Long productId = 1L;

            given(productRepository.findById(productId)).willReturn(Optional.of(product));
            //when-then
            assertThatThrownBy(() -> productService.deleteProduct(productId, 2L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("해당 상품을 삭제할 권한이 없습니다.");
        }
    }
}
package com.example.product.service;

import com.example.common.exception.GlobalException;
import com.example.member.entity.Member;
import com.example.member.entity.MemberRole;
import com.example.member.repository.MemberRepository;
import com.example.product.TestBuilder;
import com.example.product.dto.ProductOption;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.product_image.entity.ProductImage;
import com.example.product_image.repository.ProductImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 클래스의")
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    OptionRepository optionRepository;

    @Mock
    ProductImageRepository productImageRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    ProductService productService;

    @Nested
    @DisplayName("saveProduct 메서드는")
    class Describe_saveProduct {
        ProductRequestDto requestDto;
        ProductResponseDto responseDto;
        Member member;
        Product product;
        @Nested
        @DisplayName("올바른 productRequestDto이고, 멤버 권한이 manager라면")
        class Context_with_dto_authorized_member_role {
            @BeforeEach
            void setUp() {
                requestDto = TestBuilder.testProductRequestDtoBuild();
                product = TestBuilder.testProductBuild();
                member = product.getMember();
                List<ProductImage> productImages = product.getProductImages();

                given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
                given(productImageRepository.findById(1L)).willReturn(Optional.of(productImages.get(0)));
                given(productImageRepository.findById(2L)).willReturn(Optional.of(productImages.get(1)));
                given(productRepository.save(any(Product.class))).willReturn(product);
                given(optionRepository.save(any(Option.class))).willReturn(any(Option.class));

                responseDto = productService.saveProduct(requestDto, member.getId());
            }
            @Test
            @DisplayName("상품 등록에 성공하고, 해당 상품 응답 dto를 반환한다.")
            void it_returns_product_response_dto() {
                assertEquals(requestDto.getStatus(), responseDto.getStatus());
                assertEquals(requestDto.getName(), responseDto.getName());
                assertEquals(requestDto.getPrice(), responseDto.getPrice());
                assertEquals(requestDto.getInfo(), responseDto.getInfo());
                assertEquals(0, responseDto.getLikesCount());
                assertEquals(member.getNickname(), responseDto.getNickname());
                assertEquals(requestDto.getStartDate(), responseDto.getStartDate());
                assertEquals(requestDto.getEndDate(), responseDto.getEndDate());
                assertEquals("www.s3v1.png", responseDto.getImageUrls().get(0));
                assertEquals("www.s3v2.png", responseDto.getImageUrls().get(1));
                assertEquals(requestDto.getOptions(), responseDto.getOptions());
            }
        }
        @Nested
        @DisplayName("올바른 productRequestDto이고, 유효하지 않은 memberId일 경우")
        class Context_with_dto_un_authorized_member_role {
            @BeforeEach
            void setUp() {
                member = TestBuilder.testMemberBuild();
                given(memberRepository.findById(member.getId())).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("권환 없음 예외를 발생시킨다.")
            void it_returns_un_authorized_exception() {
                assertThatThrownBy(() -> productService.saveProduct(requestDto, member.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("아이디가 일치하지 않습니다.");
            }
        }
    }

    @Nested
    @DisplayName("findProduct 메서드는")
    class Describe_findProduct {
        Product product;
        Member member;
        ProductResponseDto responseDto;
        @Nested
        @DisplayName("존재하는 상품일 경우")
        class Context_with_exist_product {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();

                given(productRepository.findFetchById(product.getId())).willReturn(Optional.of(product));

                responseDto = productService.findProduct(product.getId());
            }
            @Test
            @DisplayName("상품 정보를 반환한다.")
            void it_returns_product_response_dto() {
                assertEquals(product.getStatus(), responseDto.getStatus());
                assertEquals(product.getName(), responseDto.getName());
                assertEquals(product.getPrice(), responseDto.getPrice());
                assertEquals(product.getInfo(), responseDto.getInfo());
                assertEquals(0, responseDto.getLikesCount());
                assertEquals(product.getMember().getNickname(), responseDto.getNickname());
                assertEquals(product.getStartDate(), responseDto.getStartDate());
                assertEquals(product.getEndDate(), responseDto.getEndDate());
                assertEquals("www.s3v1.png", responseDto.getImageUrls().get(0));
                assertEquals("www.s3v2.png", responseDto.getImageUrls().get(1));
                assertEquals(product.getOptions().size(), responseDto.getOptions().size());
            }
        }
        @Nested
        @DisplayName("존재하지 않는 상품일 경우")
        class Context_with_not_exist_product {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();
                given(productRepository.findFetchById(product.getId())).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("존재하지 않는 상품 예외를 발생시킨다.")
            void it_returns_not_found_exception() {
                assertThatThrownBy(() -> productService.findProduct(product.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 찾을 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("modifyProduct 메서드는")
    class Describe_modifyProduct {
        Product product;
        Member member;
        Member member2;
        ProductRequestDto requestDto;
        ProductResponseDto responseDto;
        @Nested
        @DisplayName("존재하는 상품, 올바른 productRequestDto, 해당 상품의 memberId일 경우")
        class Context_with_productId_productRequestDto_memberId {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();
                requestDto = TestBuilder.testProductRequestDtoBuild();
                member = product.getMember();
                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
                doNothing().when(optionRepository).deleteAllByProductId(product.getId());
                given(optionRepository.save(any(Option.class))).willReturn(any(Option.class));

                responseDto = productService.modifyProduct(product.getId(), requestDto, member.getId());
            }
            @Test
            @DisplayName("상품 수정에 성공하고, 수정된 상품 정보를 반환한다.")
            void it_returns_modified_product_response() {
                assertEquals(requestDto.getStatus(), responseDto.getStatus());
                assertEquals(requestDto.getName(), responseDto.getName());
                assertEquals(requestDto.getPrice(), responseDto.getPrice());
                assertEquals(requestDto.getInfo(), responseDto.getInfo());
                assertEquals(0, responseDto.getLikesCount());
                assertEquals(member.getNickname(), responseDto.getNickname());
                assertEquals(requestDto.getStartDate(), responseDto.getStartDate());
                assertEquals(requestDto.getEndDate(), responseDto.getEndDate());
                assertEquals("www.s3v1.png", responseDto.getImageUrls().get(0));
                assertEquals("www.s3v2.png", responseDto.getImageUrls().get(1));
                assertEquals(requestDto.getOptions(), responseDto.getOptions());
            }
        }
        @Nested
        @DisplayName("존재하지 않는 상품일 경우")
        class Context_with_not_found_product {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();
                requestDto = TestBuilder.testProductRequestDtoBuild();
                member = product.getMember();
            }
            @Test
            @DisplayName("존재하지 않는 상품 예외를 발생시킨다.")
            void it_returns_not_found_exception() {
                assertThatThrownBy(() -> productService.modifyProduct(product.getId(), requestDto, member.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 찾을 수 없습니다.");
            }
        }
        @Nested
        @DisplayName("해당 상품에 대한 권한이 없는 memberId일 경우")
        class Context_with_un_authorized {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();
                requestDto = TestBuilder.testProductRequestDtoBuild();
                member2 = new Member(2L, "username2", "password1234@", "nickname2"
                        , "member2@naver.com", MemberRole.manager);

                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
            }
            @Test
            @DisplayName("권한 없음 예외를 발생시킨다.")
            void it_returns_un_authorized_exception() {
                assertThatThrownBy(() -> productService.modifyProduct(product.getId(), requestDto, member2.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 수정할 권한이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메서드는")
    class Describe_deleteProduct {
        Product product;
        ProductRequestDto requestDto;
        Member member;
        Member member2;
        @Nested
        @DisplayName("존재하는 상품이고 해당 상품에 권한이 있는 member인 경우")
        class Context_with_productId_authorized_member {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();
                requestDto = TestBuilder.testProductRequestDtoBuild();
                member = product.getMember();

                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
                doNothing().when(optionRepository).deleteAllByProductId(product.getId());
                doNothing().when(productImageRepository).deleteAllByProductId(product.getId());
                doNothing().when(productRepository).deleteById(product.getId());

                productService.deleteProduct(product.getId(), member.getId());
            }
            @Test
            @DisplayName("상품 삭제에 성공한다.")
            void it_returns_success_delete() {
                then(optionRepository).should(times(1)).deleteAllByProductId(product.getId());
                then(productImageRepository).should(times(1)).deleteAllByProductId(product.getId());
                then(productRepository).should(times(1)).deleteById(product.getId());
            }
        }
        @Nested
        @DisplayName("존재하지 않은 상품일 경우")
        class Context_with_not_found_product {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(1L)).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("존재하지 않는 상품 예외를 발생시킨다.")
            void it_returns_not_found_exception() {
                assertThatThrownBy(() -> productService.deleteProduct(1L, 1L))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 찾을 수 없습니다.");
            }
        }
        @Nested
        @DisplayName("해당 상품에 대한 권한이 없는 memberId일 경우")
        class Context_with_un_authorized {
            @BeforeEach
            void setUp() {
                product = TestBuilder.testProductBuild();
                requestDto = TestBuilder.testProductRequestDtoBuild();
                member = product.getMember();
                member2 = new Member(2L, "username2", "password1234@", "nickname2"
                        , "member2@naver.com", MemberRole.manager);

                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
            }
            @Test
            @DisplayName("권한 없음 예외를 발생시킨다.")
            void it_returns_un_authorized_exception() {
                assertThatThrownBy(() -> productService.deleteProduct(product.getId(), member2.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 삭제할 권한이 없습니다.");
            }
        }
    }
}
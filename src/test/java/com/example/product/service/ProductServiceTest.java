package com.example.product.service;

import com.example.common.exception.GlobalException;
import com.example.common.global.PageResponseDto;
import com.example.likes.entity.Likes;
import com.example.likes.repository.LikesRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.ProductTestBuilder;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.dto.response.ProductThumbNailDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Mock
    LikesRepository likesRepository;

    @InjectMocks
    ProductService productService;

    Product product;
    Member member;
    Member member2;
    ProductRequestDto requestDto;
    ProductResponseDto responseDto;
    Page<Product> products;
    PageResponseDto<ProductThumbNailDto> pageResponseDto;
    Pageable pageable;
    String keyword = "가톨릭";

    Likes likes;

    @Nested
    @DisplayName("saveProduct 메서드는")
    class Describe_saveProduct {
        @Nested
        @DisplayName("올바른 productRequestDto이고, 유효한 memberId 경우")
        class Context_with_dto_exist_member {
            @BeforeEach
            void setUp() {
                requestDto = ProductTestBuilder.testProductRequestDtoBuild();
                product = ProductTestBuilder.testProductBuild();
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
                assertEquals(requestDto.getOptions().get(0).getName(), responseDto.getOptions().get(0).getName());
                assertEquals(requestDto.getOptions().get(0).getAdditionalPrice()
                        , responseDto.getOptions().get(0).getAdditionalPrice());
                assertEquals(requestDto.getSellerAccount(), responseDto.getSellerAccount());
            }
        }
        @Nested
        @DisplayName("올바른 productRequestDto이고, 유효하지 않은 memberId인 경우")
        class Context_with_dto_un_authorized_member_role {
            @BeforeEach
            void setUp() {
                member = ProductTestBuilder.testMemberBuild();
                given(memberRepository.findById(member.getId())).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("해당 유저를 찾을 수 없다는 예외를 발생시킨다.")
            void it_returns_not_found_member_exception() {
                assertThatThrownBy(() -> productService.saveProduct(requestDto, member.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("사용자가 존재하지 않습니다.");
            }
        }
    }

    @Nested
    @DisplayName("findProduct 메서드는")
    class Describe_findProduct {

        @Nested
        @DisplayName("유효한 productId이고 내가 좋아요를 누른 상품인 경우")
        class Context_with_exist_product_and_likes {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member2 = ProductTestBuilder.testMember2Build();
                Likes likes = new Likes();

                given(productRepository.findFetchById(product.getId())).willReturn(Optional.of(product));
                given(likesRepository.findByProductIdAndMemberId(product.getId(), member2.getId())).willReturn(Optional.of(likes));
                responseDto = productService.findProduct(product.getId(), member2.getId());
            }
            @Test
            @DisplayName("상품 정보와 isLiked = true를 반환한다.")
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
                assertEquals(true, responseDto.getIsLiked());
                assertEquals(product.getMember().getId(), responseDto.getSellerId());
                assertEquals(product.getSellerAccount(), responseDto.getSellerAccount());
            }
        }

        @Nested
        @DisplayName("유효한 productId이고 내가 좋아요를 누르지 않은 상품인 경우")
        class Context_with_exist_product_and_not_likes {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member2 = ProductTestBuilder.testMember2Build();

                given(productRepository.findFetchById(product.getId())).willReturn(Optional.of(product));
                given(likesRepository.findByProductIdAndMemberId(product.getId(), member2.getId())).willReturn(Optional.empty());
                responseDto = productService.findProduct(product.getId(), member2.getId());
            }
            @Test
            @DisplayName("상품 정보와 isLiked = false 반환한다.")
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
                assertEquals(false, responseDto.getIsLiked());
                assertEquals(product.getMember().getId(), responseDto.getSellerId());
                assertEquals(product.getSellerAccount(), responseDto.getSellerAccount());
            }
        }
        @Nested
        @DisplayName("유효하지 않은 productId인 경우")
        class Context_with_not_exist_product {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member2 = ProductTestBuilder.testMember2Build();
                given(productRepository.findFetchById(product.getId())).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("존재하지 않는 상품 예외를 발생시킨다.")
            void it_returns_not_found_product_exception() {
                assertThatThrownBy(() -> productService.findProduct(product.getId(), member2.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 찾을 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("modifyProduct 메서드는")
    class Describe_modifyProduct {
        @Nested
        @DisplayName("유효하는 productId, 올바른 productRequestDto, 해당 상품이 memberId가 등록한 상품인 경우")
        class Context_with_productId_productRequestDto_memberId {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                requestDto = ProductTestBuilder.testProductRequestDtoBuild();
                member = product.getMember();
                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
                given(productImageRepository.findById(requestDto.getProductImageIds().get(0))).willReturn(Optional.of(ProductTestBuilder.testProductImageBuild()));
                given(productImageRepository.findById(requestDto.getProductImageIds().get(1))).willReturn(Optional.of(ProductTestBuilder.testProductImage2Build()));

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
                assertEquals(ProductTestBuilder.testProductImageBuild().getImageUrl(), responseDto.getImageUrls().get(0));
                assertEquals(ProductTestBuilder.testProductImage2Build().getImageUrl(), responseDto.getImageUrls().get(1));
                assertEquals(requestDto.getOptions().get(0).getName(), responseDto.getOptions().get(0).getName());
                assertEquals(requestDto.getOptions().get(0).getAdditionalPrice()
                        , responseDto.getOptions().get(0).getAdditionalPrice());
                assertEquals(requestDto.getSellerAccount(), responseDto.getSellerAccount());
            }
        }
        @Nested
        @DisplayName("유효하지 않은 productId인 경우")
        class Context_with_not_found_product {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member = product.getMember();
            }
            @Test
            @DisplayName("존재하지 않는 상품 예외를 발생시킨다.")
            void it_returns_not_found_product_exception() {
                assertThatThrownBy(() -> productService.modifyProduct(product.getId(), requestDto, member.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 찾을 수 없습니다.");
            }
        }
        @Nested
        @DisplayName("해당 상품을 등록하지 않은 memberId인 경우")
        class Context_with_un_authorized {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member2 = ProductTestBuilder.testMember2Build();

                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
            }
            @Test
            @DisplayName("해당 상품을 수정할 권한 없음 예외를 발생시킨다.")
            void it_returns_unauthorized_modify_product_exception() {
                assertThatThrownBy(() -> productService.modifyProduct(product.getId(), requestDto, member2.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 수정할 권한이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메서드는")
    class Describe_deleteProduct {
        @Nested
        @DisplayName("유효한 productId이고 해당 상품을 등록한 memberId인 경우")
        class Context_with_productId_authorized_member {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member = product.getMember();

                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
                doNothing().when(productImageRepository).deleteAllByProductId(product.getId());
                doNothing().when(productRepository).deleteById(product.getId());

                productService.deleteProduct(product.getId(), member.getId());
            }
            @Test
            @DisplayName("상품 삭제에 성공한다.")
            void it_returns_success_delete() {
                then(productImageRepository).should(times(1)).deleteAllByProductId(product.getId());
                then(productRepository).should(times(1)).deleteById(product.getId());
            }
        }
        @Nested
        @DisplayName("유효하지 않은 productId인 경우")
        class Context_with_not_found_product {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(1L)).willReturn(Optional.empty());
            }
            @Test
            @DisplayName("존재하지 않는 상품 예외를 발생시킨다.")
            void it_returns_not_found_product_exception() {
                assertThatThrownBy(() -> productService.deleteProduct(1L, 1L))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 찾을 수 없습니다.");
            }
        }
        @Nested
        @DisplayName("해당 상품을 등록하지 않은 memberId인 경우")
        class Context_with_un_authorized {
            @BeforeEach
            void setUp() {
                product = ProductTestBuilder.testProductBuild();
                member = product.getMember();
                member2 = ProductTestBuilder.testMember2Build();

                given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
            }
            @Test
            @DisplayName("해당 상품을 삭제할 권한 없음 예외를 발생시킨다.")
            void it_returns_unauthorized_delete_product_exception() {
                assertThatThrownBy(() -> productService.deleteProduct(product.getId(), member2.getId()))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("해당 상품을 삭제할 권한이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("pagingProduct 메서드는")
    class Describe_pagingProduct {
        @Nested
        @DisplayName("페이지 번호로 조회 후, 결과가 존재하는 경우")
        class Context_with_page_num {
            @BeforeEach
            void setUp() {
                products = ProductTestBuilder.testPageProductBuild();
                for (Product product : products) {
                    given(productImageRepository.findFirstByProductId(product.getId()))
                            .willReturn(Optional.of(product.getProductImages().get(0)));
                }
                given(productRepository.findAll(any(Pageable.class))).willReturn(products);
                pageResponseDto = productService.pagingProduct(ProductTestBuilder.testPageableBuild());
            }
            @Test
            @DisplayName("해당 페이지 번호의 결과를 반환한다.")
            void it_returns_products_in_page_num() {
                assertEquals(products.getTotalPages(), pageResponseDto.getTotalPage());
                assertEquals(products.getTotalElements(), pageResponseDto.getTotalElements());
                assertEquals(products.getContent().size() , pageResponseDto.getListSize());
                assertEquals(products.getContent().get(0).getName(), pageResponseDto.getContent().get(0).getProductName());
                assertEquals(products.isFirst(), pageResponseDto.isFirst());
                assertEquals(products.isLast(), pageResponseDto.isLast());
            }
        }
        @Nested
        @DisplayName("결과가 존재하지 않는 경우")
        class Context_with_empty_paging_result {
            @BeforeEach
            void setUp() {
                pageable = ProductTestBuilder.testPageableBuild();
                given(productRepository.findAll(pageable)).willReturn(Page.empty());
            }
            @Test
            @DisplayName("상품을 찾을 수 없다는 예외를 발생시킨다.")
            void it_returns_not_found_paging_product_exception() {
                assertThatThrownBy(() -> productService.pagingProduct(pageable))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("목록에 상품이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("searchProduct 메서드는")
    class Describe_searchProduct {
        @Nested
        @DisplayName("keyword로 검색한 결과가 존재하는 경우")
        class Context_with_keyword_page_num {
            @BeforeEach
            void setUp() {
                products = ProductTestBuilder.testPageProductBuild();
                for (Product product : products) {
                    given(productImageRepository.findFirstByProductId(product.getId()))
                            .willReturn(Optional.of(product.getProductImages().get(0)));
                }

                given(productRepository.findSearchByKeyword(keyword, pageable)).willReturn(products);
                pageResponseDto = productService.searchProduct(keyword, pageable);
            }
            @Test
            @DisplayName("해당 페이지 번호의 결과를 반환한다.")
            void it_returns_products_in_page_num() {
                assertEquals(products.getTotalPages(), pageResponseDto.getTotalPage());
                assertEquals(products.getTotalElements(), pageResponseDto.getTotalElements());
                assertEquals(products.getContent().size() , pageResponseDto.getListSize());
                assertEquals(products.getContent().get(0).getName(), pageResponseDto.getContent().get(0).getProductName());
                assertEquals(products.isFirst(), pageResponseDto.isFirst());
                assertEquals(products.isLast(), pageResponseDto.isLast());
            }
        }
        @Nested
        @DisplayName("검색 결과가 존재하지 않는 경우")
        class Context_with_empty_paging_result {
            @BeforeEach
            void setUp() {
                pageable = ProductTestBuilder.testPageableBuild();
                given(productRepository.findSearchByKeyword(keyword, pageable)).willReturn(Page.empty());
            }
            @Test
            @DisplayName("상품을 찾을 수 없다는 예외를 발생시킨다.")
            void it_returns_not_found_search_product_exception() {
                assertThatThrownBy(() -> productService.searchProduct(keyword, pageable))
                        .isInstanceOf(GlobalException.class)
                        .hasMessage("검색 결과 해당 상품이 없습니다.");
            }
        }
    }
}
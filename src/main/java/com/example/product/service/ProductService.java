package com.example.product.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.entity.Goods;
import com.example.product.entity.Jacket;
import com.example.product.entity.Product;
import com.example.product.enums.ProductType;
import com.example.product.repository.ProductRepository;
import com.example.product_image.entity.ProductImage;
import com.example.product_image.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto, Long memberId) {
        List<Long> productImageIds = productRequestDto.getProductImageIds();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_MEMBER)
                );

        if (productRequestDto.getType().equals(ProductType.잠바)) {
            Jacket jacket = productRequestDto.toJacket(); //Jacket엔티티 생성

            for (Long productImageId : productImageIds) {
                ProductImage productImage = productImageRepository.findById(productImageId)
                        .orElseThrow(
                                () -> new GlobalException(BaseErrorCode.NOT_FOUND_IMAGE)
                        ); //업로드한 이미지 엔티티
                jacket.addProductImage(productImage);
                jacket.addMember(member);
            }

            Jacket savedJacket = productRepository.save(jacket);
            return ProductResponseDto.toResponseDto(savedJacket);
        } else {
            Goods goods = productRequestDto.toGoods();

            for (Long productImageId : productImageIds) {
                ProductImage productImage = productImageRepository.findById(productImageId)
                        .orElseThrow(
                                () -> new GlobalException(BaseErrorCode.NOT_FOUND_IMAGE)
                        ); //업로드한 이미지 엔티티
                goods.addProductImage(productImage);
                goods.addMember(member);
            }

            Goods savedGoods = productRepository.save(goods);
            return ProductResponseDto.toResponseDto(savedGoods);
        }
    }

    public ProductResponseDto findProduct(Long productId) {
        Product product = productRepository.findFetchById(productId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_PRODUCT)
                );
        return ProductResponseDto.toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto modifyProduct(Long productId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_PRODUCT)
                );

        if (productRequestDto.getType().equals(ProductType.잠바)) {
            Jacket jacket = (Jacket) product;
            jacket.modify(productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getInfo()
                    , productRequestDto.getType(), productRequestDto.getStatus(), productRequestDto.getStartDate()
                    , productRequestDto.getEndDate(), productRequestDto.getColors(), productRequestDto.getSizes());

            return ProductResponseDto.toResponseDto(jacket);
        } else {
            Goods goods = (Goods) product;
            goods.modify(productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getInfo()
                    , productRequestDto.getType(), productRequestDto.getStatus(), productRequestDto.getStartDate()
                    , productRequestDto.getEndDate(), productRequestDto.getColors());

            return ProductResponseDto.toResponseDto(goods);
        }
    }
}

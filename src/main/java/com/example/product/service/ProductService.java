package com.example.product.service;

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
    public Long saveProduct(ProductRequestDto productRequestDto, Long memberId) {
        List<Long> productImageIds = productRequestDto.getProductImageIds();
        Member member = memberRepository.findById(memberId).orElseThrow();

        if (productRequestDto.getType().equals(ProductType.잠바)) {
            Jacket jacket = productRequestDto.toJacket(); //Jacket엔티티 생성

            for (Long productImageId : productImageIds) {
                ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(); //업로드한 이미지 엔티티
                jacket.addProductImage(productImage);
                jacket.addMember(member);
            }

            Jacket savedJacket = productRepository.save(jacket);
            return savedJacket.getId();
        } else {
            Goods goods = productRequestDto.toGoods();

            for (Long productImageId : productImageIds) {
                ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(); //업로드한 이미지 엔티티
                goods.addProductImage(productImage);
                goods.addMember(member);
            }

            Goods savedGoods = productRepository.save(goods);
            return savedGoods.getId();
        }
    }

    public ProductResponseDto findProduct(Long productId) {
        Product product = productRepository.findFetchById(productId).orElseThrow();
        return ProductResponseDto.toResponseDto(product);
    }

    @Transactional
    public void modifyProduct(Long productId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId).orElseThrow();

        if (productRequestDto.getType().equals(ProductType.잠바)) {
            Jacket jacket = (Jacket) product;
            jacket.modify(productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getInfo()
                    , productRequestDto.getType(), productRequestDto.getStatus(), productRequestDto.getStartDate()
                    , productRequestDto.getEndDate(), productRequestDto.getColors(), productRequestDto.getSizes());
        } else {
            Goods goods = (Goods) product;
            goods.modify(productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getInfo()
                    , productRequestDto.getType(), productRequestDto.getStatus(), productRequestDto.getStartDate()
                    , productRequestDto.getEndDate(), productRequestDto.getColors());
        }
    }
}

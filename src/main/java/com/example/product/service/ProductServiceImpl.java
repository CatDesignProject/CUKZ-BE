package com.example.product.service;

import com.example.product.dto.request.ProductRequestDto;
import com.example.product.entity.Goods;
import com.example.product.entity.Jacket;
import com.example.product.repository.ProductRepository;
import com.example.product_image.entity.ProductImage;
import com.example.product_image.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    @Override
    public Long saveProduct(ProductRequestDto productRequestDto) {
        List<Long> productImageIds = productRequestDto.getProductImageIds();

        if (String.valueOf(productRequestDto.getType()).equals("잠바")) {
            Jacket jacket = productRequestDto.toJacket(); //Jacket엔티티 생성
            for (Long productImageId : productImageIds) {
                ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(); //업로드한 이미지 엔티티
                jacket.addProductImage(productImage);
            }

            Jacket savedJacket = productRepository.save(jacket);
            return savedJacket.getId();

        } else {
            Goods goods = productRequestDto.toGoods();

            for (Long productImageId : productImageIds) {
                ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(); //업로드한 이미지 엔티티
                goods.addProductImage(productImage);
            }

            Goods savedGoods = productRepository.save(goods);
            return savedGoods.getId();
        }

    }
}

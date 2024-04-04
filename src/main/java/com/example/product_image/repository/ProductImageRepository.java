package com.example.product_image.repository;

import com.example.product_image.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteAllByProductId(Long productId);
    Optional<ProductImage> findFirstByProductId(Long productId);
}

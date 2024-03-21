package com.example.product_image.repository;


import com.example.product_image.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteAllByProductId(Long productId);
}

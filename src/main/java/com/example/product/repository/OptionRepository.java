package com.example.product.repository;

import com.example.product.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
    void deleteAllByProductId(Long productId);
}

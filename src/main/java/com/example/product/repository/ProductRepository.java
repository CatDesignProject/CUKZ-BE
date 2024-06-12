package com.example.product.repository;

import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p join fetch p.productImages where p.id = :productId")
    Optional<Product> findFetchById(@Param("productId") Long productId);

    @Query("select p from Product p where p.name like %:keyword%")
    Page<Product> findSearchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Product> findByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE DATE(p.endDate) < :date AND p.status = :status ORDER BY p.endDate DESC")
    List<Product> findEndedProduct(
            @Param("date") LocalDate date,
            @Param("status") SaleStatus status);
}

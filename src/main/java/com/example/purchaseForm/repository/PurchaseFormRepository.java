package com.example.purchaseForm.repository;

import com.example.purchaseForm.entity.PurchaseForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PurchaseFormRepository extends JpaRepository<PurchaseForm, Long> {
    @Query("SELECT pf FROM PurchaseForm pf WHERE pf.memberId = :orderNumber")
    Optional<PurchaseForm> findByOrderNumber(Long orderNumber);

    Page<PurchaseForm> findByMemberId(Long memberId, Pageable pageable);

    Page<PurchaseForm> findByProductId(Long productId, Pageable pageable);
}

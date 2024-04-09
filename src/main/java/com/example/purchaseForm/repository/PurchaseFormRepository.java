package com.example.purchaseForm.repository;

import com.example.purchaseForm.entity.PurchaseForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseFormRepository extends JpaRepository<PurchaseForm, Long> {
    Optional<PurchaseForm> findByIdAndMemberId(Long purchaseFormId, Long memberId);
}

package com.example.purchaseForm.repository;

import com.example.purchaseForm.entity.PurchaseForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseFormRepository extends JpaRepository<PurchaseForm, Long> {
}

package com.example.purchaseForm.repository;

import com.example.purchaseForm.entity.PurchaseOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOptionRepository extends JpaRepository<PurchaseOption, Long> {
}

package com.example.purchaseForm.repository;

import com.example.purchaseForm.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    void deleteByProductId(Long productId);
}

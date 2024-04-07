package com.example.demandForm.repository;

import com.example.demandForm.entity.DemandForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DemandFormRepository extends JpaRepository<DemandForm, Long> {

    Optional<DemandForm> findByProductIdAndMemberId(Long productId, Long memberId);

    Optional<DemandForm> findByIdAndMemberId(Long demandFormId, Long memberId);

    @Query("SELECT df FROM DemandForm df WHERE df.memberId = :orderNumber")
    Optional<DemandForm> findByOrderNumber(@Param("orderNumber") Long orderNumber);

    Optional<DemandForm> findByEmail(String email);

    Page<DemandForm> findByMemberId(Long memberId, Pageable pageable);

    Page<DemandForm> findByProductId(Long productId, Pageable pageable);
}

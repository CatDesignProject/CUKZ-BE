package com.example.demandForm.repository;

import com.example.demandForm.entity.DemandForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemandFormRepository extends JpaRepository<DemandForm, Long> {

    Optional<DemandForm> findByProductIdAndMemberId(Long productId, Long memberId);

    Page<DemandForm> findByMemberId(Long memberId, Pageable pageable);
}

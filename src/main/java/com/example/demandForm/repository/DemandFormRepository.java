package com.example.demandForm.repository;

import com.example.demandForm.entity.DemandForm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandFormRepository extends JpaRepository<DemandForm, Long> {

    Optional<DemandForm> findByProductIdAndMemberId(Long productId, Long memberId);
}

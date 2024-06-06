package com.example.demandForm.repository;

import com.example.demandForm.entity.DemandOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandOptionRepository extends JpaRepository<DemandOption, Long> {

    @Query("SELECT d.optionId AS optionId, d.optionName AS optionName, d.additionalPrice AS additionalPrice, SUM(d.quantity) AS quantity " +
            "FROM DemandOption d WHERE d.productId = :productId GROUP BY d.optionId, d.optionName, d.additionalPrice")
    List<DemandOptionSummary> findSummarizedByProductId(@Param("productId") Long productId);

    interface DemandOptionSummary {
        Long getOptionId();

        String getOptionName();

        int getAdditionalPrice();

        int getQuantity();
    }
}

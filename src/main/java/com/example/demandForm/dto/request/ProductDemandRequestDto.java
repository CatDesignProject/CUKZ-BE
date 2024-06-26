package com.example.demandForm.dto.request;

import com.example.product.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDemandRequestDto {

    private SaleStatus saleStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

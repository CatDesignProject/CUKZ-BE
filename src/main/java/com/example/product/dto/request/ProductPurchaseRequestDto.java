package com.example.product.dto.request;

import com.example.product.enums.SaleStatus;
import com.example.purchaseForm.dto.DeliveryRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPurchaseRequestDto {

    private SaleStatus saleStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<DeliveryRequestDto> deliveryList;
}

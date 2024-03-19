package com.example.demandForm.dto;

import com.example.demandForm.entity.DemandForm;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemandFormResponseDto {

    private Long id;
    private Long productId;
    private int quantity;

    public DemandFormResponseDto(DemandForm demandForm) {
        this.id = demandForm.getId();
        this.productId = demandForm.getProduct().getId();
        this.quantity = demandForm.getQuantity();
    }
}

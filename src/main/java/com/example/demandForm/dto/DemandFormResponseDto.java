package com.example.demandForm.dto;

import com.example.demandForm.entity.DemandForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandFormResponseDto {

    private Long id;
    private Long productId;
    private int quantity;

    public static DemandFormResponseDto toResponseDto(DemandForm demandForm) {
        return DemandFormResponseDto.builder()
            .id(demandForm.getId())
            .productId(demandForm.getProduct().getId())
            .quantity(demandForm.getQuantity())
            .build();
    }
}

package com.example.demandForm.dto.response;

import com.example.demandForm.entity.DemandOption;
import com.example.purchaseForm.entity.PurchaseOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormOptionResponseDto {

    private Long optionId;
    private int quantity;

    public static FormOptionResponseDto toResponseDto(DemandOption demandOption) {

        return FormOptionResponseDto.builder()
                .optionId(demandOption.getOption().getId())
                .quantity(demandOption.getQuantity())
                .build();
    }

    public static FormOptionResponseDto toResponseDto(PurchaseOption purchaseOption) {

        return FormOptionResponseDto.builder()
                .optionId(purchaseOption.getOption().getId())
                .quantity(purchaseOption.getQuantity())
                .build();
    }
}

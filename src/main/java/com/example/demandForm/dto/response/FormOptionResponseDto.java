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
    private String optionName;
    private int additionalPrice;
    private int quantity;

    public static FormOptionResponseDto toResponseDto(DemandOption demandOption) {

        return FormOptionResponseDto.builder()
                .optionId(demandOption.getOptionId())
                .optionName(demandOption.getOptionName())
                .additionalPrice(demandOption.getAdditionalPrice())
                .quantity(demandOption.getQuantity())
                .build();
    }

    public static FormOptionResponseDto toResponseDto(PurchaseOption purchaseOption) {

        return FormOptionResponseDto.builder()
                .optionId(purchaseOption.getOption().getId())
                .optionName(purchaseOption.getOption().getName())
                .quantity(purchaseOption.getQuantity())
                .build();
    }
}

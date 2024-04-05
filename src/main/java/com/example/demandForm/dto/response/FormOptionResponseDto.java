package com.example.demandForm.dto.response;

import com.example.demandForm.entity.DemandOption;
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
}

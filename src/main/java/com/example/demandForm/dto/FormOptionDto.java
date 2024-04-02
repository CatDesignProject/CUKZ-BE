package com.example.demandForm.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormOptionDto {

    private Long optionId;
    
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
}

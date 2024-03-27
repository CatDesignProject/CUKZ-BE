package com.example.demandForm.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DemandFormRequestDto {

    @Min(value = 1, message = "수량은 1개 이상 입력해주세요")
    private int quantity;
}

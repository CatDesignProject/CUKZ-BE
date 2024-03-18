package com.example.demandForm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemandFormResponseDto {

    private Long id;
    private Long productId;
    private String quantity;
}

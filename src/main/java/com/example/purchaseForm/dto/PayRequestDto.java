package com.example.purchaseForm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayRequestDto {

    private boolean payStatus;
    private List<Long> purchaseFormIds;
}

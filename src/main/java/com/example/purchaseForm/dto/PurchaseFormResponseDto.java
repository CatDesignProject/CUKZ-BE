package com.example.purchaseForm.dto;

import com.example.demandForm.dto.response.FormOptionResponseDto;
import com.example.purchaseForm.entity.PurchaseForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseFormResponseDto {

    private Long id;
    private Long productId;
    private Long memberId;
    private List<FormOptionResponseDto> optionList;

    public static PurchaseFormResponseDto toResponseDto(PurchaseForm purchaseForm) {

        List<FormOptionResponseDto> optionList = purchaseForm.getPurchaseOptionList().stream()
                .map(FormOptionResponseDto::toResponseDto)
                .toList();

        return PurchaseFormResponseDto.builder()
                .id(purchaseForm.getId())
                .productId(purchaseForm.getProduct().getId())
                .memberId(purchaseForm.getMember().getId())
                .optionList(optionList)
                .build();
    }
}

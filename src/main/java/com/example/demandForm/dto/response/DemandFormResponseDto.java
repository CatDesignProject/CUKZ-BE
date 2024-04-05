package com.example.demandForm.dto.response;

import com.example.demandForm.entity.DemandForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandFormResponseDto {

    private Long id;
    private Long productId;
    private Long memberId;
    private List<FormOptionResponseDto> optionList;

    public static DemandFormResponseDto toResponseDto(DemandForm demandForm) {

        List<FormOptionResponseDto> optionList = demandForm.getDemandOptionList().stream()
                .map(FormOptionResponseDto::toResponseDto)
                .toList();

        return DemandFormResponseDto.builder()
                .id(demandForm.getId())
                .productId(demandForm.getProduct().getId())
                .memberId(demandForm.getMemberId())
                .optionList(optionList)
                .build();
    }
}

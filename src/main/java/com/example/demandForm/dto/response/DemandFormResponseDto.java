package com.example.demandForm.dto.response;

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
    private Long memberId;

    public static DemandFormResponseDto toResponseDto(DemandForm demandForm) {

        return DemandFormResponseDto.builder()
                .id(demandForm.getId())
                .memberId(demandForm.getMemberId())
                .build();
    }
}

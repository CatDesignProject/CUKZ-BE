package com.example.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikesResponseDto {

    private int likesCount;

    public static LikesResponseDto toResponseDto(int likesCount) {
        return LikesResponseDto.builder()
                .likesCount(likesCount)
                .build();
    }
}

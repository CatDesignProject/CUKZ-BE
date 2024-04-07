package com.example.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {

    private String nickname;
    private int sellerKindnessCnt; //총대가 친절해요
    private int goodNotificationCnt; //진행상황 공지를 잘해줘요.
    private int descriptionMatchCnt; //상품 설명과 수령한 상품이 동일해요.
    private int arrivalSatisfactoryCnt; //상품이 잘 도착했어요.

    public static ReviewResponseDto toResponseDto(String nickname, int sellerKindnessCnt, int goodNotificationCnt, int descriptionMatchCnt, int arrivalSatisfactoryCnt) {
        return ReviewResponseDto.builder()
                .nickname(nickname)
                .sellerKindnessCnt(sellerKindnessCnt)
                .goodNotificationCnt(goodNotificationCnt)
                .descriptionMatchCnt(descriptionMatchCnt)
                .arrivalSatisfactoryCnt(arrivalSatisfactoryCnt)
                .build();
    }
}

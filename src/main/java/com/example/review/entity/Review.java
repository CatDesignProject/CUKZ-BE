package com.example.review.entity;

import com.example.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 총대
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    // 구매자 정보
    private Long buyerId;

    // 구매폼
    private Long purchaseFormId;

    // 리뷰 내용
    private boolean sellerKindness; //총대가 친절해요
    private boolean goodNotification; //진행상황 공지를 잘해줘요.
    private boolean descriptionMatch; //상품 설명과 수령한 상품이 동일해요.
    private boolean arrivalSatisfactory; //상품이 잘 도착했어요.

    public void addSeller(Member seller) {
        this.seller = seller;
    }

    public void addBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public void addPurchaseFormId(Long purchaseFormId) {
        this.purchaseFormId = purchaseFormId;
    }
}

package com.example.demandForm.entity;

import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private Long memberId;

    private boolean isMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static DemandForm toMemberEntity(Member member, Product product, DemandFormRequestDto requestDto) {

        return DemandForm.builder()
            .quantity(requestDto.getQuantity())
            .memberId(member.getId())
            .product(product)
            .isMember(true)
            .build();
    }

    public static DemandForm toNonMemberEntity(Long orderNumber, Product product, DemandFormRequestDto requestDto) {

        return DemandForm.builder()
            .quantity(requestDto.getQuantity())
            .memberId(orderNumber)
            .product(product)
            .isMember(false)
            .build();
    }
}

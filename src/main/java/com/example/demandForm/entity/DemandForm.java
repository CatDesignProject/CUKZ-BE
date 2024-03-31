package com.example.demandForm.entity;

import com.example.common.time.TimeStamp;
import com.example.demandForm.dto.CreateDemandFormRequestDto;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandForm extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private boolean isMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static DemandForm toMemberEntity(Member member, Product product, CreateDemandFormRequestDto requestDto) {

        return DemandForm.builder()
                .memberId(member.getId())
                .product(product)
                .isMember(true)
                .build();
    }

    public static DemandForm toNonMemberEntity(Long orderNumber, Product product, CreateDemandFormRequestDto requestDto) {

        return DemandForm.builder()
                .memberId(orderNumber)
                .product(product)
                .isMember(false)
                .build();
    }
}

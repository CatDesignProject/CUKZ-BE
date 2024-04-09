package com.example.demandForm.entity;

import com.example.common.time.TimeStamp;
import com.example.demandForm.dto.request.DemandFormRequestDto;
import com.example.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Email
    private String email;

    @OneToMany(mappedBy = "demandForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandOption> demandOptionList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static DemandForm toEntity(Long memberId, Product product, DemandFormRequestDto requestDto) {

        return DemandForm.builder()
                .memberId(memberId)
                .product(product)
                .isMember(true)
                .email(requestDto.getEmail())
                .demandOptionList(new ArrayList<>())
                .build();
    }
}

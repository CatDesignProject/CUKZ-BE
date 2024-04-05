package com.example.purchaseForm.entity;

import com.example.common.time.TimeStamp;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseForm extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String address;

    private String phone;

    private boolean payStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "purchaseForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOption> purchaseOptionList;
}

package com.example.purchaseForm.entity;

import com.example.common.time.TimeStamp;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private String buyerName;
    private String buyerPhone;
    private String buyerEmail;

    private String RecipientName;
    private String RecipientPhone;
    private String address;

    private String payerName;
    private LocalDate payDate;
    private Boolean payStatus;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "purchaseForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOption> purchaseOptionList;
}

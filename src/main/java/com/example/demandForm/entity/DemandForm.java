package com.example.demandForm.entity;

import com.example.member.entity.Member;
import com.example.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public DemandForm(int quantity, Member member, Product product) {
        this.quantity = quantity;
        this.memberId = member.getId();
        this.product = product;
        this.isMember = true;
    }

    public DemandForm(int quantity, Long orderNumber, Product product) {
        this.quantity = quantity;
        this.memberId = orderNumber;
        this.product = product;
        this.isMember = false;
    }
}

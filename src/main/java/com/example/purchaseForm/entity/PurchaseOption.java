package com.example.purchaseForm.entity;

import com.example.product.entity.Option;
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
public class PurchaseOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_form_id")
    private PurchaseForm purchaseForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    public static PurchaseOption toEntity(int quantity, PurchaseForm purchaseForm, Option option) {

        return PurchaseOption.builder()
                .quantity(quantity)
                .purchaseForm(purchaseForm)
                .option(option)
                .build();
    }
}

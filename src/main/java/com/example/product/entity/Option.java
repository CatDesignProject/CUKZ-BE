package com.example.product.entity;

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
@Table(name = "product_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int additionalPrice;
    private int salesQuantity;
    private int demandQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void addProduct(Product product) {
        this.product = product;
        product.addOption(this);
    }

    public void updateDemandQuantity(int quantity) {
        this.demandQuantity += quantity;
    }

    public void updateSalesQuantity(int quantity) {
        this.salesQuantity += quantity;
    }
}

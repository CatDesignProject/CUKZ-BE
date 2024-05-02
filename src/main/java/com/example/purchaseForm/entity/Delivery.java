package com.example.purchaseForm.entity;

import com.example.product.entity.Product;
import com.example.purchaseForm.dto.DeliveryRequestDto;
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
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static Delivery toEntity(DeliveryRequestDto deliveryRequestDto, Product product) {
        return Delivery.builder()
                .type(deliveryRequestDto.getType())
                .price(deliveryRequestDto.getPrice())
                .product(product)
                .build();
    }
}

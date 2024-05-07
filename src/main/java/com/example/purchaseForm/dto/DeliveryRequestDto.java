package com.example.purchaseForm.dto;

import com.example.product.entity.Product;
import com.example.purchaseForm.entity.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequestDto {

    private String type;
    private int price;

    public Delivery toEntity(Product product) {
        return Delivery.builder()
                .type(this.type)
                .price(this.price)
                .product(product)
                .build();
    }
}

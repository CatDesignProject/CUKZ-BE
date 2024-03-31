package com.example.product.dto;

import com.example.product.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionDto {

    private String name;
    private int additionalPrice;

    public Option toOption() {
        return Option.builder()
                .name(this.name)
                .additionalPrice(this.additionalPrice)
                .build();
    }
}

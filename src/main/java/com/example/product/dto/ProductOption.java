package com.example.product.dto;

import com.example.product.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOption {

    private String name;
    private int additionalPrice;
    private int salesQuantity;

    public Option toOption() {
        return Option.builder()
                .name(this.name)
                .additionalPrice(this.additionalPrice)
                .salesQuantity(this.salesQuantity)
                .build();
    }

    public static Option testBuild() {
        return Option.builder()
                .name("블랙 S")
                .additionalPrice(3000)
                .salesQuantity(100)
                .build();

    }
}

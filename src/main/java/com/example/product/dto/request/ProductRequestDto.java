package com.example.product.dto.request;


import com.example.product.entity.Goods;
import com.example.product.entity.Jacket;
import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import com.example.product.enums.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ProductRequestDto {

    private ProductType type;
    private SaleStatus status;
    private String name;
    private int price;
    private String info;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> colors;
    private List<Size> sizes;
    @NotEmpty(message = "상품 이미지를 선택해주세요.")
    private List<Long> productImageIds;

    public Jacket toJacket() {
        Jacket jacket = new Jacket();
        jacket.updateProductPart(name, price, info, type, status, startDate, endDate);
        jacket.updateJacketPart(colors, sizes);

        return jacket;
    }

    public Goods toGoods() {
        Goods goods = new Goods();
        goods.updateProductPart(name, price, info, type, status, startDate, endDate);
        goods.updateGoodsPart(this.colors);

        return goods;
    }
}

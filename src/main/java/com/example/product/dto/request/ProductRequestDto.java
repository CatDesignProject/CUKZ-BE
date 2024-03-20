package com.example.product.dto.request;


import com.example.product.entity.Goods;
import com.example.product.entity.Jacket;
import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import com.example.product.enums.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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
    private List<Long> productImageIds;

    public Jacket toJacket() {
        Jacket jacket = new Jacket();
        jacket.updateProductPart(name, price, info, type, status, startDate, endDate);
        jacket.updateJacketPart(String.valueOf(colors.get(0)), sizes);

        return jacket;
    }

    public Goods toGoods() {
        Goods goods = new Goods();
        goods.updateProductPart(name, price, info, type, status, startDate, endDate);
        goods.updateGoodsPart(this.colors);

        return goods;
    }
}

package com.example.product.entity;


import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("G")
public class Goods extends Product {

    //(색상 컬렉션 테이블 생성됌), (클래스명+필드명)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    private List<String> colors = new ArrayList<>();

    //상품 정보 수정 -> 엔티티 수정(부모 엔티티까지)
    public void modify(String name, int price, String info, ProductType type
            , SaleStatus status, LocalDateTime startDate, LocalDateTime endDate, List<String> colors) {
        super.updateProductPart(name, price, info, type, status, startDate, endDate);
        this.colors.clear();
        this.colors.addAll(colors);
    }

    public void updateGoodsPart(List<String> colors) {
        this.colors = colors;
    }

}

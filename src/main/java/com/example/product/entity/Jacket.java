package com.example.product.entity;

import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import com.example.product.enums.Size;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("J")
public class Jacket extends Product {

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    private List<String> colors = new ArrayList<>();

    // 선택 가능한 사이즈 (사이즈 전용 테이블 생성됌), (클래스명+필드명)
    @ElementCollection(targetClass = Size.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Size> availableSizes = new ArrayList<>();

    //상품 정보 수정 -> 엔티티 수정(부모 엔티티까지)
    public void modify(String name, int price, String info, ProductType type, SaleStatus status
            , LocalDateTime startDate, LocalDateTime endDate, List<String> colors, List<Size> sizes) {
        super.updateProductPart(name, price, info, type, status, startDate, endDate);
        this.colors.clear();
        this.colors.addAll(colors);
        availableSizes.clear();
        availableSizes.addAll(sizes);
    }

    public void updateJacketPart(List<String> color, List<Size> availableSizes) {
        this.colors = color;
        this.availableSizes = availableSizes;
    }
}

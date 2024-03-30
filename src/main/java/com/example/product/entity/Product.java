package com.example.product.entity;

import com.example.member.entity.Member;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private String info;

    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Option> options = new ArrayList<>();

    //== 편의 메서드 ==//
    public void addProductImage(ProductImage productImage) {
        this.productImages.add(productImage);
        productImage.addProduct(this);
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void createProductPart(String name, int price, String info
            , SaleStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.likesCount = 0;
    }

    public void modifyProduct(String name, int price, String info
            , SaleStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateDate(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateStatus(SaleStatus status) {
        this.status = status;
    }
}

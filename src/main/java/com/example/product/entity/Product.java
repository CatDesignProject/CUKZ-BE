package com.example.product.entity;

import com.example.member.entity.Member;
import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;
    private int price;
    private String info;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();


    //== 편의 메서드 ==//
    public void addProductImage(ProductImage productImage) {
        this.productImages.add(productImage);
        productImage.setProduct(this);
    }

    public void updateProductPart(String name, int price, String info, ProductType type
            , SaleStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

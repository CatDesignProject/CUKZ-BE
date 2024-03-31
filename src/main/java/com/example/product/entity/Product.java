package com.example.product.entity;

import com.example.member.entity.Member;
import com.example.product.enums.ProductType;
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

    private int likesCount;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();


    //== 편의 메서드 ==//
    public void addProductImage(ProductImage productImage) {
        this.productImages.add(productImage);
        productImage.setProduct(this);
    }

    public void addMember(Member member) {
        this.member = member;
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

    public void updateDate(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateStatus(SaleStatus status) {
        this.status = status;
    }

    public int updateLikesCount(int cal) {
        this.likesCount += cal;
        return this.likesCount;
    }
}

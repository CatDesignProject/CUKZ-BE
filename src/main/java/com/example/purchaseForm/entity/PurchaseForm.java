package com.example.purchaseForm.entity;

import com.example.common.time.TimeStamp;
import com.example.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseForm extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerName;
    private String buyerPhone;
    private String buyerEmail;

    private Long deliveryId;
    private String recipientName;
    private String recipientPhone;
    private String address;

    private String payerName;
    private LocalDate payDate;
    private Boolean payStatus;

    private String refundName;
    private String refundAccount;

    private int totalPrice;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "purchaseForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOption> purchaseOptionList;

    public void updateTotalPrice(int price) {
        this.totalPrice += price;
    }

    public void updatePayStatus(boolean payStatus) {
        this.payStatus = payStatus;
    }
}

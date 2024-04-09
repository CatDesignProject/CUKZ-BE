package com.example.purchaseForm.entity;

import com.example.common.time.TimeStamp;
import com.example.product.entity.Product;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public static PurchaseForm toEntity(Long memberId, Product product, PurchaseFormRequestDto requestDto) {

        return PurchaseForm.builder()
                .memberId(memberId)
                .product(product)
                .buyerName(requestDto.getBuyerName())
                .buyerPhone(requestDto.getBuyerPhone())
                .buyerEmail(requestDto.getBuyerEmail())
                .deliveryId(requestDto.getDeliveryId())
                .recipientName(requestDto.getRecipientName())
                .recipientPhone(requestDto.getRecipientPhone())
                .address(requestDto.getAddress())
                .payerName(requestDto.getPayerName())
                .payDate(requestDto.getPayDate())
                .payStatus(requestDto.getPayStatus())
                .refundName(requestDto.getRefundName())
                .refundAccount(requestDto.getRefundAccount())
                .purchaseOptionList(new ArrayList<>())
                .build();
    }

    public void updateTotalPrice(int price) {
        this.totalPrice += price;
    }
}

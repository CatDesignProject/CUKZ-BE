package com.example.purchaseForm.dto;

import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.product.entity.Product;
import com.example.purchaseForm.entity.PurchaseForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormRequestDto {

    private String buyerName;
    private String buyerPhone;
    //private String buyerEmail;

    //private Long deliveryId;
    private String recipientName;
    private String recipientPhone;
    private String address;

    private String payerName;

    private String refundName;
    private String refundAccount;

    private List<FormOptionRequestDto> optionList;

    public PurchaseForm toEntity(Long memberId, Product product) {

        return PurchaseForm.builder()
                .memberId(memberId)
                .product(product)
                .buyerName(this.buyerName)
                .buyerPhone(this.buyerPhone)
                //.buyerEmail(this.buyerEmail)
                //.deliveryId(this.deliveryId)
                .recipientName(this.recipientName)
                .recipientPhone(this.recipientPhone)
                .address(this.address)
                .payerName(this.payerName)
                .payStatus(false)
                .refundName(this.refundName)
                .refundAccount(this.refundAccount)
                .purchaseOptionList(new ArrayList<>())
                .build();
    }
}

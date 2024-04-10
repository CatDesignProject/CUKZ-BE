package com.example.purchaseForm;

import com.example.demandForm.DemandTest;
import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;

import java.util.List;

import static com.example.demandForm.DemandTestBuilder.buildFormOption;
import static com.example.demandForm.DemandTestBuilder.buildOption;
import static com.example.product.ProductTestBuilder.testProductBuild;

public class PurchaseTestBuilder implements PurchaseTest, DemandTest {

    public static PurchaseFormRequestDto buildPurchaseFormRequestDto() {
        List<FormOptionRequestDto> optionList = List.of(
                buildFormOption(1L),
                buildFormOption(2L)
        );

        return PurchaseFormRequestDto.builder()
                .buyerName(BUYER_NAME)
                .buyerPhone(BUYER_PHONE)
                .buyerEmail(BUYER_EMAIL)
                .deliveryId(1L)
                .recipientName(RECIPIENT_NAME)
                .recipientPhone(RECIPIENT_PHONE)
                .address(ADDRESS)
                .payerName(PAYER_NAME)
                .payDate(PAY_DATE)
                .refundName(REFUND_NAME)
                .refundAccount(REFUND_ACCOUNT)
                .optionList(optionList)
                .build();
    }

    public static PurchaseForm buildPurchaseForm() {
        return PurchaseForm.toEntity(1L, testProductBuild(), buildPurchaseFormRequestDto());
    }

    public static PurchaseOption buildPurchaseOption() {
        return PurchaseOption.toEntity(QUANTITY, buildPurchaseForm(), buildOption());
    }
}

package com.example.purchaseForm.dto;

import com.example.demandForm.dto.request.FormOptionRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormRequestDto {

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

    private List<FormOptionRequestDto> optionList;
}

package com.example.demandForm.dto.request;

import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.entity.DemandOption;
import com.example.product.entity.Option;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormOptionRequestDto {

    private Long optionId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;

    public PurchaseOption toEntity(PurchaseForm purchaseForm, Option option) {

        return PurchaseOption.builder()
                .quantity(this.quantity)
                .purchaseForm(purchaseForm)
                .optionId(optionId)
                .optionName(option.getName())
                .additionalPrice(option.getAdditionalPrice())
                .build();
    }

    public DemandOption toEntity(DemandForm demandForm, Option option) {

        return DemandOption.builder()
                .quantity(this.quantity)
                .demandForm(demandForm)
                .optionId(optionId)
                .optionName(option.getName())
                .additionalPrice(option.getAdditionalPrice())
                .build();
    }
}

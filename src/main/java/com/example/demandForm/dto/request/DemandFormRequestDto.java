package com.example.demandForm.dto.request;

import com.example.demandForm.entity.DemandForm;
import com.example.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DemandFormRequestDto {

    private String email;

    private List<FormOptionRequestDto> optionList;

    public DemandForm toEntity(Long memberId, Product product) {

        return DemandForm.builder()
                .memberId(memberId)
                .product(product)
                .isMember(true)
                .email(this.email)
                .demandOptionList(new ArrayList<>())
                .build();
    }
}
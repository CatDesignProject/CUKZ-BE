package com.example.demandForm;

import com.example.demandForm.dto.request.DemandFormRequestDto;
import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.entity.DemandOption;
import com.example.member.entity.Member;
import com.example.product.entity.Option;
import com.example.product.entity.Product;

import java.util.List;

public class DemandTestBuilder implements DemandTest {
    public static DemandFormRequestDto buildCreateDemandFormRequestDto() {
        List<FormOptionRequestDto> optionList = List.of(
                new FormOptionRequestDto(1L, QUANTITY),
                new FormOptionRequestDto(2L, QUANTITY)
        );

        return new DemandFormRequestDto(TEST_EMAIL, optionList);
    }

    public static Option buildOption() {
        return Option.builder()
                .id(1L)
                .name(OPTION_NAME)
                .build();
    }

    public static DemandOption buildDemandOption(DemandForm demandForm) {
        return DemandOption.toEntity(QUANTITY, demandForm, buildOption());
    }

    public static DemandForm buildMemberDemandForm(Member member, Product product) {
        return DemandForm.toMemberEntity(member, product, buildCreateDemandFormRequestDto());
    }

    public static DemandForm buildNonMemberDemandForm(Product product) {
        return DemandForm.toNonMemberEntity(ORDER_NUMBER, product, buildCreateDemandFormRequestDto());
    }
}

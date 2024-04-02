package com.example.demandForm;

import com.example.demandForm.dto.CreateDemandFormRequestDto;
import com.example.demandForm.dto.FormOptionDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.entity.DemandOption;
import com.example.member.entity.Member;
import com.example.product.entity.Option;
import com.example.product.entity.Product;

import java.util.List;

public class DemandTestBuilder implements DemandTest {
    public static CreateDemandFormRequestDto buildCreateDemandFormRequestDto() {
        List<FormOptionDto> optionList = List.of(
                new FormOptionDto(1L, QUANTITY),
                new FormOptionDto(2L, QUANTITY)
        );

        return new CreateDemandFormRequestDto(TEST_EMAIL, optionList);
    }

    public static Option buildOption() {
        return Option.builder()
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

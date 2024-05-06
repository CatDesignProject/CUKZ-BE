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
                buildFormOption(1L),
                buildFormOption(2L)
        );

        return new DemandFormRequestDto(TEST_EMAIL, optionList);
    }

    public static FormOptionRequestDto buildFormOption(Long id) {
        return new FormOptionRequestDto(id, QUANTITY);
    }

    public static Option buildOption() {
        return Option.builder()
                .id(1L)
                .name(OPTION_NAME)
                .build();
    }

    public static Option buildOption(int additionalPrice) {
        return Option.builder()
                .id(1L)
                .additionalPrice(additionalPrice)
                .name(OPTION_NAME)
                .build();
    }

    public static DemandOption buildDemandOption(DemandForm demandForm) {
        FormOptionRequestDto requestDto = new FormOptionRequestDto(1L, QUANTITY);
        return requestDto.toEntity(demandForm, buildOption());
    }

    public static DemandForm buildMemberDemandForm(Member member, Product product) {
        DemandFormRequestDto requestDto = buildCreateDemandFormRequestDto();
        return requestDto.toEntity(member.getId(), product);
    }

    public static DemandForm buildNonMemberDemandForm(Product product) {
        DemandFormRequestDto requestDto = buildCreateDemandFormRequestDto();
        return requestDto.toEntity(ORDER_NUMBER, product);
    }
}

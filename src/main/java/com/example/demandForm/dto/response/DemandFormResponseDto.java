package com.example.demandForm.dto.response;

import com.example.demandForm.entity.DemandForm;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandFormResponseDto {

    private Long id;
    private Long memberId;
    private String email;
    private Long productId;
    private String productName;
    private int price;
    private SaleStatus status;
    private List<String> imageUrlList;
    private List<FormOptionResponseDto> optionList;

    public static DemandFormResponseDto toResponseDto(DemandForm demandForm) {

        List<String> imageUrlList = demandForm.getProduct().getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .toList();

        List<FormOptionResponseDto> optionList = demandForm.getDemandOptionList().stream()
                .map(FormOptionResponseDto::toResponseDto)
                .toList();

        return DemandFormResponseDto.builder()
                .id(demandForm.getId())
                .memberId(demandForm.getMemberId())
                .email(demandForm.getEmail())
                .productId(demandForm.getProduct().getId())
                .productName(demandForm.getProduct().getName())
                .price(demandForm.getProduct().getPrice())
                .status(demandForm.getProduct().getStatus())
                .imageUrlList(imageUrlList)
                .optionList(optionList)
                .build();
    }
}

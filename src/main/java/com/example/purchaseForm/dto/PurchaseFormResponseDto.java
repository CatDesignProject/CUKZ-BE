package com.example.purchaseForm.dto;

import com.example.demandForm.dto.response.FormOptionResponseDto;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product_image.entity.ProductImage;
import com.example.purchaseForm.entity.PurchaseForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseFormResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private int price;
    private SaleStatus status;
    private Long memberId;
    private int totalPrice;
    private List<String> imageUrlList;
    private List<FormOptionResponseDto> optionList;

    public static PurchaseFormResponseDto toResponseDto(PurchaseForm purchaseForm) {

        Product product = purchaseForm.getProduct();

        List<FormOptionResponseDto> optionList = purchaseForm.getPurchaseOptionList().stream()
                .map(FormOptionResponseDto::toResponseDto)
                .toList();

        List<String> imageUrlList = product.getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .toList();

        return PurchaseFormResponseDto.builder()
                .id(purchaseForm.getId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .memberId(purchaseForm.getMemberId())
                .totalPrice(purchaseForm.getTotalPrice())
                .optionList(optionList)
                .imageUrlList(imageUrlList)
                .build();
    }
}

package com.example.product.dto.request;

import com.example.product.dto.ProductOptionDto;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private SaleStatus status;
    private String name;
    private int price;
    private String info;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @NotEmpty(message = "상품 이미지를 선택해주세요.")
    private List<Long> productImageIds;
    private List<ProductOptionDto> options;
    private String sellerAccount;

    public Product toProduct() {
        Product product = new Product();
        product.createProductPart(name, price, info, status, startDate, endDate, sellerAccount);
        return product;
    }
}
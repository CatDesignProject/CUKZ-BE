package com.example.product.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.common.global.PageResponseDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.dto.ProductOptionDto;
import com.example.product.dto.request.ProductPurchaseRequestDto;
import com.example.product.dto.request.ProductRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.dto.response.ProductThumbNailDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.product_image.entity.ProductImage;
import com.example.product_image.repository.ProductImageRepository;
import com.example.purchaseForm.dto.DeliveryRequestDto;
import com.example.purchaseForm.entity.Delivery;
import com.example.purchaseForm.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductImageRepository productImageRepository;
    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_MEMBER)
                );

        Product product = productRequestDto.toProduct();

        List<Long> productImageIds = productRequestDto.getProductImageIds();
        for (Long productImageId : productImageIds) {
            ProductImage productImage = productImageRepository.findById(productImageId)
                    .orElseThrow(
                            () -> new GlobalException(BaseErrorCode.NOT_FOUND_IMAGE)
                    );
            product.addProductImage(productImage);
        }
        product.addMember(member);
        productRepository.save(product);

        List<ProductOptionDto> productOptionDtos = productRequestDto.getOptions();
        for (ProductOptionDto productOptionDto : productOptionDtos) {
            Option option = productOptionDto.toOption();
            option.addProduct(product);
            optionRepository.save(option);
        }

        return ProductResponseDto.toResponseDto(product);
    }

    public ProductResponseDto findProduct(Long productId) {
        Product product = productRepository.findFetchById(productId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_PRODUCT)
                );

        return ProductResponseDto.toResponseDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_PRODUCT)
                );

        if (!product.getMember().getId().equals(memberId)) {
            throw new GlobalException(BaseErrorCode.UNAUTHORIZED_DELETE_PRODUCT);
        }
        optionRepository.deleteAllByProductId(productId);
        productImageRepository.deleteAllByProductId(productId);
        productRepository.deleteById(productId);
    }

    @Transactional
    public ProductResponseDto modifyProduct(Long productId, ProductRequestDto productRequestDto, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_PRODUCT)
                );

        if (!product.getMember().getId().equals(memberId)) {
            throw new GlobalException(BaseErrorCode.UNAUTHORIZED_MODIFY_PRODUCT);
        }

        product.modifyProduct(productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getInfo(), productRequestDto.getStatus(),
                productRequestDto.getStartDate(), productRequestDto.getEndDate());

        optionRepository.deleteAllByProductId(productId);
        List<ProductOptionDto> productOptionDtos = productRequestDto.getOptions();
        for (ProductOptionDto productOptionDto : productOptionDtos) {
            Option option = productOptionDto.toOption();
            option.addProduct(product);
            optionRepository.save(option);
        }

        return ProductResponseDto.toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto modifyProductPurchase(Long productId, ProductPurchaseRequestDto requestDto, Long memberId) {

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new GlobalException(BaseErrorCode.NOT_FOUND_PRODUCT));

        if (!product.getMember().getId().equals(memberId)) {
            throw new GlobalException(BaseErrorCode.UNAUTHORIZED_MODIFY_PRODUCT);
        }

        checkPeriod(requestDto);
        product.modifyProductForm(requestDto);
        if (!requestDto.getDeliveryList().isEmpty()) {
            saveDeliveryList(requestDto, product);
        }

        return ProductResponseDto.toResponseDto(product);
    }

    public PageResponseDto<ProductThumbNailDto> pagingProduct(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new GlobalException(BaseErrorCode.NOT_FOUND_PAGING_PRODUCT);
        }
        return toProductThumbNailDto(page);
    }

    public PageResponseDto<ProductThumbNailDto> searchProduct(String keyword, Pageable pageable) {
        Page<Product> page = productRepository.findSearchByKeyword(keyword, pageable);
        if (page.isEmpty()) {
            throw new GlobalException(BaseErrorCode.NOT_FOUND_SEARCH_PRODUCT);
        }
        return toProductThumbNailDto(page);
    }

    private PageResponseDto<ProductThumbNailDto> toProductThumbNailDto(Page<Product> page) {
        Page<ProductThumbNailDto> productThumbNailDtos = page.map(
                product ->
                        new ProductThumbNailDto(
                                product.getName(), product.getPrice(), product.getLikesCount()
                                , productImageRepository.findFirstByProductId(product.getId())
                                .orElseThrow(
                                        () -> new GlobalException(BaseErrorCode.NOT_FOUND_IMAGE)
                                )
                                .getImageUrl()
                                , product.getMember().getNickname()
                        )
        );
        return PageResponseDto.toResponseDto(productThumbNailDtos);
    }

    private void saveDeliveryList(ProductPurchaseRequestDto requestDto, Product product) {
        // 배송지 리스트 저장
        for (DeliveryRequestDto deliveryDto : requestDto.getDeliveryList()) {
            Delivery delivery = Delivery.toEntity(deliveryDto, product);
            deliveryRepository.save(delivery);
            product.getDeliveries().add(delivery);
        }
    }

    private void checkPeriod(ProductPurchaseRequestDto requestDto) {
        // 시작일이 종료일보다 늦음
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new GlobalException(BaseErrorCode.INVALID_PERIOD);
        }

        // 판매중, 수요조사중 상태인데 종료일이 지남
        if ((requestDto.getSaleStatus().equals(SaleStatus.ON_SALE) || requestDto.getSaleStatus().equals(SaleStatus.ON_DEMAND))
                && requestDto.getEndDate().isBefore(LocalDateTime.now())) {
            throw new GlobalException(BaseErrorCode.INVALID_STATUS);
        }
    }
}
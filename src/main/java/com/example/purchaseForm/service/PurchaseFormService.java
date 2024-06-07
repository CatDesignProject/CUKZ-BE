package com.example.purchaseForm.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.demandForm.dto.request.GetFormNonMemberRequestDto;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.purchaseForm.dto.*;
import com.example.purchaseForm.entity.Delivery;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;
import com.example.purchaseForm.repository.DeliveryRepository;
import com.example.purchaseForm.repository.PurchaseFormRepository;
import com.example.purchaseForm.repository.PurchaseOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import static com.example.common.exception.BaseErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseFormService {

    private final PurchaseFormRepository purchaseFormRepository;
    private final PurchaseOptionRepository purchaseOptionRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final DeliveryRepository deliveryRepository;

    private static final long MAX_ORDER_NUMBER = 9999999999L;

    @Transactional
    public PurchaseFormResponseDto purchaseMember(Long productId, PurchaseFormRequestDto requestDto, Long memberId) {

        // 판매 기간 검증
        Product product = findProduct(productId);
        checkPeriod(product);

        // purchase form 생성
        PurchaseForm purchaseForm = requestDto.toEntity(memberId, product);
        purchaseFormRepository.save(purchaseForm);

        // 옵션 리스트에 대한 정보 저장
        saveOptions(requestDto, purchaseForm, product);

        // 배송 금액 저장
//        Delivery delivery = findDelivery(requestDto.getDeliveryId());
//        purchaseForm.updateTotalPrice(delivery.getPrice());

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }

    @Transactional
    public PurchaseFormResponseDto purchaseNonMember(Long productId, PurchaseFormRequestDto requestDto) {

        // 판매 기간 검증
        Product product = findProduct(productId);
        checkPeriod(product);

        // 랜덤 주문번호 생성
        long orderNumber = generateOrderNumber();
        PurchaseForm purchaseForm = requestDto.toEntity(orderNumber, product);
        purchaseFormRepository.save(purchaseForm);
        saveOptions(requestDto, purchaseForm, product);

        // 배송 금액 저장
//        Delivery delivery = findDelivery(requestDto.getDeliveryId());
//        purchaseForm.updateTotalPrice(delivery.getPrice());

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }

    @Transactional(readOnly = true)
    public Page<PurchaseFormResponseDto> getAllPurchaseFormsMember(int page, int size, Long memberId) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseForm> purchaseFormList = purchaseFormRepository.findByMemberId(memberId, pageable);

        return purchaseFormList.map(PurchaseFormResponseDto::toResponseDto);
    }

    @Transactional(readOnly = true)
    public PurchaseFormResponseDto getPurchaseFormMember(Long purchaseFormId, Long memberId) {

        PurchaseForm purchaseForm = findPurchaseForm(purchaseFormId);
        checkMember(purchaseForm, memberId);

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }


    @Transactional(readOnly = true)
    public PurchaseFormResponseDto getPurchaseFormNonMember(GetFormNonMemberRequestDto requestDto) {

        PurchaseForm purchaseForm = purchaseFormRepository.findByOrderNumber(requestDto.getOrderNumber())
                .orElseThrow(() -> new GlobalException(NOT_FOUND_FORM));

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }

    @Transactional
    public void deletePurchaseForm(Long purchaseFormId) {

        PurchaseForm purchaseForm = findPurchaseForm(purchaseFormId);

        purchaseForm.getPurchaseOptionList().forEach(purchaseOption -> {
            Option option = findOption(purchaseOption.getOptionId());
            option.updateSalesQuantity(-purchaseOption.getQuantity());
        });

        purchaseFormRepository.delete(purchaseForm);
    }

    @Transactional(readOnly = true)
    public Page<PurchaseFormResponseDto> getAllPurchaseForms(int page, int size, Long productId, Long memberId) {

        Product product = findProduct(productId);
        checkMember(product, memberId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseForm> purchaseFormList = purchaseFormRepository.findByProductId(productId, pageable);

        return purchaseFormList.map(PurchaseFormResponseDto::toResponseDto);
    }

    @Transactional
    public void updatePayStatus(Long productId, PayRequestDto requestDto, Long memberId) {

        Product product = findProduct(productId);
        checkMember(product, memberId);

        for (Long formId : requestDto.getPurchaseFormIds()) {
            PurchaseForm purchaseForm = findPurchaseForm(formId);
            purchaseForm.updatePayStatus(requestDto.isPayStatus());
        }
    }

    @Transactional
    public ProductResponseDto modifyPurchaseForm(Long productId, ProductPurchaseRequestDto requestDto, Long memberId) {

        // 유저 권한 검증, 날짜 검증
        Product product = findProduct(productId);
        checkMember(product, memberId);
        checkPeriod(requestDto);

        // 폼 관련 정보 수정, 배송지 저장
        product.modifyProductForm(requestDto.getSaleStatus(), requestDto.getStartDate(), requestDto.getEndDate());
        saveDeliveryList(requestDto, product);

        return ProductResponseDto.toResponseDto(product);
    }

    private long generateOrderNumber() {
        // 비회원 주문 번호 생성 = 현재 날짜 + 랜덤 숫자 (총 16자리)
        LocalDate today = LocalDate.now();
        String dateString = String.format("%ty%tm%td", today, today, today);

        Random random = new Random();
        long randomNumber = random.nextLong() % MAX_ORDER_NUMBER;
        randomNumber = Math.abs(randomNumber);

        String orderNumberStr = dateString + String.format("%010d", randomNumber);
        return Long.parseLong(orderNumberStr);
    }

    private void saveOptions(PurchaseFormRequestDto requestDto, PurchaseForm purchaseForm, Product product) {

        int price = product.getPrice();

        for (FormOptionRequestDto optionDto : requestDto.getOptionList()) {
            // 옵션 수요수량 업데이트
            Option option = findOption(optionDto.getOptionId());
            option.updateSalesQuantity(optionDto.getQuantity());

            // 총 가격 업데이트
            purchaseForm.updateTotalPrice((price + option.getAdditionalPrice()) * optionDto.getQuantity());

            // 옵션 수요조사 내역 저장
            PurchaseOption purchaseOption = optionDto.toEntity(purchaseForm, option);
            purchaseOptionRepository.save(purchaseOption);
            purchaseForm.getPurchaseOptionList().add(purchaseOption);
        }
    }

    private void saveDeliveryList(ProductPurchaseRequestDto requestDto, Product product) {
        // 기존 배송지 리스트 삭제
        deliveryRepository.deleteByProductId(product.getId());

        // 신규 배송지 리스트 저장
        for (DeliveryRequestDto deliveryDto : requestDto.getDeliveryList()) {
            Delivery delivery = deliveryDto.toEntity(product);
            deliveryRepository.save(delivery);
        }
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_PRODUCT)
        );
    }

    private Option findOption(Long optionId) {
        return optionRepository.findById(optionId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_OPTION)
        );
    }

//    private Delivery findDelivery(Long deliveryId) {
//        return deliveryRepository.findById(deliveryId).orElseThrow(() ->
//                new GlobalException(NOT_FOUND_DELIVERY)
//        );
//    }

    private PurchaseForm findPurchaseForm(Long formId) {
        return purchaseFormRepository.findById(formId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_FORM)
        );
    }

    public void checkMember(Product product, Long memberId) {
        if (!product.getMember().getId().equals(memberId)) {
            throw new GlobalException(UNAUTHORIZED_MEMBER);
        }
    }

    public void checkMember(PurchaseForm purchaseForm, Long memberId) {
        if (!purchaseForm.getMemberId().equals(memberId)) {
            throw new GlobalException(UNAUTHORIZED_MEMBER);
        }
    }

    public void checkPeriod(Product product) {
        // 판매 기간이 아님
        LocalDateTime now = LocalDateTime.now();
        log.info("now: {}", now);
        if (now.isAfter(product.getEndDate()) || now.isBefore(product.getStartDate())) {
            throw new GlobalException(NOT_IN_PERIOD);
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

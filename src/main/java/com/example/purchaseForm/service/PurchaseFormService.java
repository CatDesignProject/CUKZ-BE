package com.example.purchaseForm.service;

import com.example.common.exception.GlobalException;
import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import com.example.purchaseForm.dto.PurchaseFormResponseDto;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;
import com.example.purchaseForm.repository.PurchaseFormRepository;
import com.example.purchaseForm.repository.PurchaseOptionRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PurchaseFormService {

    private final PurchaseFormRepository purchaseFormRepository;
    private final PurchaseOptionRepository purchaseOptionRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;

    private static final long MAX_ORDER_NUMBER = 9999999999L;

    @Transactional
    public PurchaseFormResponseDto purchaseMember(Long productId, PurchaseFormRequestDto requestDto, Long memberId) {

        // 판매 기간 검증
        Product product = findProduct(productId);
        checkPeriod(product);

        // purchase form 생성
        PurchaseForm purchaseForm = PurchaseForm.toEntity(memberId, product, requestDto);
        purchaseFormRepository.save(purchaseForm);

        // 옵션 리스트에 대한 정보 저장
        saveOptions(requestDto, purchaseForm);

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }

    @Transactional
    public PurchaseFormResponseDto purchaseNonMember(Long productId, PurchaseFormRequestDto requestDto) {

        // 판매 기간 검증
        Product product = findProduct(productId);
        checkPeriod(product);

        // 랜덤 주문번호 생성
        long orderNumber = generateOrderNumber();
        PurchaseForm purchaseForm = PurchaseForm.toEntity(orderNumber, product, requestDto);
        purchaseFormRepository.save(purchaseForm);
        saveOptions(requestDto, purchaseForm);

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }

    @Transactional(readOnly = true)
    public PurchaseFormResponseDto getPurchaseFormMember(Long purchaseFormId, Long memberId) {

        PurchaseForm purchaseForm = purchaseFormRepository.findByIdAndMemberId(purchaseFormId, memberId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_FORM));

        return PurchaseFormResponseDto.toResponseDto(purchaseForm);
    }

    @Transactional(readOnly = true)
    public Page<PurchaseFormResponseDto> getAllPurchaseFormsMember(int page, int size, Long memberId) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseForm> purchaseFormList = purchaseFormRepository.findByMemberId(memberId, pageable);

        return purchaseFormList.map(PurchaseFormResponseDto::toResponseDto);
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

    private void saveOptions(PurchaseFormRequestDto requestDto, PurchaseForm purchaseForm) {

        for (FormOptionRequestDto optionDto : requestDto.getOptionList()) {
            // 옵션 수요수량 업데이트
            Option option = findOption(optionDto.getOptionId());
            option.updateSalesQuantity(optionDto.getQuantity());

            // 옵션 수요조사 내역 저장
            PurchaseOption purchaseOption = PurchaseOption.toEntity(optionDto.getQuantity(), purchaseForm, option);
            purchaseOptionRepository.save(purchaseOption);
            purchaseForm.getPurchaseOptionList().add(purchaseOption);
        }
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_MEMBER)
        );
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

    public void checkPeriod(Product product) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(product.getEndDate()) || now.isBefore(product.getStartDate())) {
            throw new GlobalException(NOT_IN_PERIOD);
        }
    }
}

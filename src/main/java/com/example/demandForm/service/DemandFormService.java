package com.example.demandForm.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.demandForm.dto.request.DemandFormRequestDto;
import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.demandForm.dto.request.GetFormNonMemberRequestDto;
import com.example.demandForm.dto.request.ProductDemandRequestDto;
import com.example.demandForm.dto.response.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.entity.DemandOption;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.demandForm.repository.DemandOptionRepository;
import com.example.product.dto.response.ProductResponseDto;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
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
public class DemandFormService {

    private final DemandFormRepository demandFormRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final DemandOptionRepository demandOptionRepository;

    private static final long MAX_ORDER_NUMBER = 9999999999L;

    @Transactional
    public DemandFormResponseDto demandMember(Long productId, DemandFormRequestDto requestDto, Long memberId) {

        // 유저 중복 참여 검증
        checkDuplicate(requestDto.getEmail(), productId);

        // 수요조사 기간 검증
        Product product = findProduct(productId);
        checkPeriod(product);

        // demand form 생성
        DemandForm demandForm = requestDto.toEntity(memberId, product);
        demandFormRepository.save(demandForm);

        // 옵션 리스트에 대한 정보 저장
        saveOptions(requestDto, demandForm);

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional
    public DemandFormResponseDto demandNonMember(Long productId, DemandFormRequestDto requestDto) {

        // 중복 참여, 수요조사 기간 검증
        checkDuplicate(requestDto.getEmail(), productId);
        Product product = findProduct(productId);
        checkPeriod(product);

        // 랜덤 주문번호 생성
        long orderNumber = generateOrderNumber();
        DemandForm demandForm = requestDto.toEntity(orderNumber, product);
        demandFormRepository.save(demandForm);
        saveOptions(requestDto, demandForm);

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional(readOnly = true)
    public DemandFormResponseDto getDemandFormMember(Long demandFormId, Long memberId) {

        DemandForm demandForm = findDemandForm(demandFormId);
        checkMember(demandForm, memberId);

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional(readOnly = true)
    public Page<DemandFormResponseDto> getAllDemandFormsMember(int page, int size, Long memberId) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DemandForm> demandFormList = demandFormRepository.findByMemberId(memberId, pageable);

        return demandFormList.map(DemandFormResponseDto::toResponseDto);
    }

    @Transactional(readOnly = true)
    public DemandFormResponseDto getDemandFormNonMember(GetFormNonMemberRequestDto requestDto) {

        DemandForm demandForm = demandFormRepository.findByOrderNumber(requestDto.getOrderNumber())
                .orElseThrow(() -> new GlobalException(NOT_FOUND_FORM));

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional
    public void deleteDemandForm(Long demandFormId) {

        DemandForm demandForm = findDemandForm(demandFormId);

        demandForm.getDemandOptionList().forEach(demandOption -> {
            Option option = demandOption.getOption();
            option.updateDemandQuantity(-demandOption.getQuantity());
        });

        demandFormRepository.delete(demandForm);
    }

    @Transactional(readOnly = true)
    public Page<DemandFormResponseDto> getAllDemandForms(int page, int size, Long productId, Long memberId) {

        Product product = findProduct(productId);
        checkMember(product, memberId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DemandForm> demandFormList = demandFormRepository.findByProductId(productId, pageable);

        return demandFormList.map(DemandFormResponseDto::toResponseDto);
    }

    @Transactional
    public ProductResponseDto modifyDemandForm(Long productId, ProductDemandRequestDto requestDto, Long memberId) {

        Product product = findProduct(productId);
        checkMember(product, memberId);
        checkPeriod(requestDto);
        product.modifyProductForm(requestDto.getSaleStatus(), requestDto.getStartDate(), requestDto.getEndDate());

        return ProductResponseDto.toResponseDto(product);
    }

    public long generateOrderNumber() {
        // 비회원 주문 번호 생성 = 현재 날짜 + 랜덤 숫자 (총 16자리)
        LocalDate today = LocalDate.now();
        String dateString = String.format("%ty%tm%td", today, today, today);

        Random random = new Random();
        long randomNumber = random.nextLong() % MAX_ORDER_NUMBER;
        randomNumber = Math.abs(randomNumber);

        String orderNumberStr = dateString + String.format("%010d", randomNumber);
        return Long.parseLong(orderNumberStr);
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

    private DemandForm findDemandForm(Long formId) {
        return demandFormRepository.findById(formId).orElseThrow(() ->
                new GlobalException(NOT_FOUND_FORM)
        );
    }

    public void checkMember(Product product, Long memberId) {
        if (!product.getMember().getId().equals(memberId)) {
            throw new GlobalException(UNAUTHORIZED_MEMBER);
        }
    }

    public void checkMember(DemandForm demandForm, Long memberId) {
        if (!demandForm.getMemberId().equals(memberId)) {
            throw new GlobalException(UNAUTHORIZED_MEMBER);
        }
    }

    public void checkDuplicate(String email, Long productId) {
        if (demandFormRepository.findByEmailAndProductId(email, productId).isPresent()) {
            throw new GlobalException(DUPLICATED_FORM);
        }
    }

    public void checkPeriod(Product product) {
        // 수요조사 기간이 아님
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(product.getEndDate()) || now.isBefore(product.getStartDate())) {
            throw new GlobalException(NOT_IN_PERIOD);
        }
    }

    private void checkPeriod(ProductDemandRequestDto requestDto) {
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

    private void saveOptions(DemandFormRequestDto requestDto, DemandForm demandForm) {

        for (FormOptionRequestDto optionDto : requestDto.getOptionList()) {
            // 옵션 수요수량 업데이트
            Option option = findOption(optionDto.getOptionId());
            option.updateDemandQuantity(optionDto.getQuantity());

            // 옵션 수요조사 내역 저장
            DemandOption demandOption = optionDto.toEntity(demandForm, option);
            demandOptionRepository.save(demandOption);
            demandForm.getDemandOptionList().add(demandOption);
        }
    }
}

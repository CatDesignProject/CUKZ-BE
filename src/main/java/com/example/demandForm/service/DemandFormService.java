package com.example.demandForm.service;

import com.example.common.exception.GlobalException;
import com.example.demandForm.dto.request.CreateDemandFormRequestDto;
import com.example.demandForm.dto.request.DemandFormNonMemberRequestDto;
import com.example.demandForm.dto.request.FormOptionRequestDto;
import com.example.demandForm.dto.request.UpdateDemandFormRequestDto;
import com.example.demandForm.dto.response.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.entity.DemandOption;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.demandForm.repository.DemandOptionRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final DemandOptionRepository demandOptionRepository;

    private static final long MAX_ORDER_NUMBER = 9999999999L;

    @Transactional
    public DemandFormResponseDto demandMember(Long productId, CreateDemandFormRequestDto requestDto, Long memberId) {

        // 유저 중복 참여 검증
        Member member = findMember(memberId);
        checkDuplicate(requestDto.getEmail());

        // 수요조사 기간 검증
        Product product = findProduct(productId);
        checkPeriod(product);

        // demand form 생성
        DemandForm demandForm = DemandForm.toMemberEntity(member, product, requestDto);
        demandFormRepository.save(demandForm);

        // 옵션 리스트에 대한 정보 저장
        saveOptions(requestDto, demandForm);

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional
    public DemandFormResponseDto demandNonMember(Long productId, CreateDemandFormRequestDto requestDto) {

        // 중복 참여, 수요조사 기간 검증
        checkDuplicate(requestDto.getEmail());
        Product product = findProduct(productId);
        checkPeriod(product);

        // 랜덤 주문번호 생성
        long orderNumber = generateOrderNumber();
        DemandForm demandForm = DemandForm.toNonMemberEntity(orderNumber, product, requestDto);
        demandFormRepository.save(demandForm);
        saveOptions(requestDto, demandForm);

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional(readOnly = true)
    public DemandFormResponseDto getDemandFormMember(Long demandFormId, Long memberId) {

        DemandForm demandForm = demandFormRepository.findByIdAndMemberId(demandFormId, memberId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_FORM));

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional(readOnly = true)
    public Page<DemandFormResponseDto> getAllDemandFormsMember(int page, int size, Long memberId) {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DemandForm> demandFormList = demandFormRepository.findByMemberId(memberId, pageable);

        return demandFormList.map(DemandFormResponseDto::toResponseDto);
    }

    @Transactional(readOnly = true)
    public DemandFormResponseDto getDemandFormNonMember(DemandFormNonMemberRequestDto requestDto) {

        DemandForm demandForm = demandFormRepository.findByOrderNumber(requestDto.getOrderNumber()).orElseThrow(() ->
                new GlobalException(NOT_FOUND_FORM)
        );

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional(readOnly = true)
    public Page<DemandFormResponseDto> getAllDemandForms(int page, int size, Long productId, Long memberId) {

        Product product = findProduct(productId);
        checkMember(product, memberId);

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DemandForm> demandFormList = demandFormRepository.findByProductId(productId, pageable);

        return demandFormList.map(DemandFormResponseDto::toResponseDto);
    }

    @Transactional
    public void startDemandForm(Long productId, Long memberId, UpdateDemandFormRequestDto requestDto) {

        Product product = findProduct(productId);
        checkMember(product, memberId);
        product.updateDate(requestDto.getStartDate(), requestDto.getEndDate());
        product.updateStatus(SaleStatus.ON_DEMAND);
    }

    public long generateOrderNumber() {
        // 비회원 주문 번호 = 현재 날짜 + 랜덤 숫자 (16자리)
        LocalDate today = LocalDate.now();
        String dateString = String.format("%ty%tm%td", today, today, today);

        Random random = new Random();
        long randomNumber = random.nextLong() % MAX_ORDER_NUMBER;
        randomNumber = Math.abs(randomNumber);

        String orderNumberStr = dateString + String.format("%010d", randomNumber);
        return Long.parseLong(orderNumberStr);
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

    public void checkMember(Product product, Long memberId) {
        if (!product.getMember().getId().equals(memberId)) {
            throw new GlobalException(UNAUTHORIZED_MEMBER);
        }
    }

    public void checkDuplicate(String email) {
        if (demandFormRepository.findByEmail(email).isPresent()) {
            throw new GlobalException(DUPLICATED_FORM);
        }
    }

    public void checkPeriod(Product product) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(product.getEndDate()) || now.isBefore(product.getStartDate())) {
            throw new GlobalException(NOT_IN_PERIOD);
        }
    }

    private void saveOptions(CreateDemandFormRequestDto requestDto, DemandForm demandForm) {
        for (FormOptionRequestDto optionDto : requestDto.getOptionList()) {
            Option option = findOption(optionDto.getOptionId());
            DemandOption demandOption = DemandOption.toEntity(optionDto.getQuantity(), demandForm, option);
            demandOptionRepository.save(demandOption);
            demandForm.getDemandOptionList().add(demandOption);
        }
    }
}

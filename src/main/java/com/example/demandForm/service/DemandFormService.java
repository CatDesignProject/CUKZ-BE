package com.example.demandForm.service;

import com.example.common.exception.GlobalException;
import com.example.demandForm.dto.DemandFormNonMemberRequestDto;
import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.product.entity.Product;
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
import java.util.Optional;
import java.util.Random;

import static com.example.common.exception.BaseErrorCode.*;

@Service
@RequiredArgsConstructor
public class DemandFormService {

    private final DemandFormRepository demandFormRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private static final long MAX_ORDER_NUMBER = 9999999999L;

    @Transactional
    public DemandFormResponseDto demandMember(Long productId, DemandFormRequestDto requestDto, Long memberId) {

        Member member = findMember(memberId);
        Optional<DemandForm> demandFormExist = demandFormRepository.findByProductIdAndMemberId(productId, member.getId());
        if (demandFormExist.isPresent()) {
            throw new GlobalException(DUPLICATED_FORM);
        }

        Product product = findProduct(productId);
        checkPeriod(product);

        DemandForm demandForm = DemandForm.toMemberEntity(member, product, requestDto);
        demandFormRepository.save(demandForm);

        return DemandFormResponseDto.toResponseDto(demandForm);
    }

    @Transactional
    public DemandFormResponseDto demandNonMember(Long productId, DemandFormRequestDto requestDto) {

        Product product = findProduct(productId);
        checkPeriod(product);

        long orderNumber = generateOrderNumber();
        DemandForm demandForm = DemandForm.toNonMemberEntity(orderNumber, product, requestDto);
        demandFormRepository.save(demandForm);

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

        Member member = findMember(memberId);
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

    public void checkPeriod(Product product) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(product.getEndDate()) || now.isBefore(product.getStartDate())) {
            throw new GlobalException(NOT_IN_PERIOD);
        }
    }
}

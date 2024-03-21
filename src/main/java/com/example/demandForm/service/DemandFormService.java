package com.example.demandForm.service;

import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemandFormService {

    private final DemandFormRepository demandFormRepository;
    private final ProductRepository productRepository;
    private static final long MAX_ORDER_NUMBER = 9999999999L;

    @Transactional
    public DemandFormResponseDto demandMember(Long productId, DemandFormRequestDto requestDto, Member member) {

        // to-do : member role 검증

        Optional<DemandForm> demandFormExist = demandFormRepository.findByProductIdAndMemberId(
            productId, member.getId());
        if (demandFormExist.isPresent()) {
            throw new IllegalArgumentException("이미 수요조사에 참여하였습니다.");
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

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new IllegalArgumentException("상품을 찾을 수 없습니다.")
        );
    }

    public void checkPeriod(Product product){
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(product.getEndDate()) || now.isBefore(product.getStartDate())) {
            throw new IllegalArgumentException("수요조사 참여 가능 기간이 아닙니다.");
        }
    }
}

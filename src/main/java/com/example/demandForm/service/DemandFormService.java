package com.example.demandForm.service;

import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.entity.DemandForm;
import com.example.demandForm.repository.DemandFormRepository;
import com.example.member.entity.Member;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemandFormService {

    private final DemandFormRepository demandFormRepository;
    private final ProductRepository productRepository;

    public DemandFormResponseDto demand(Long productId, DemandFormRequestDto requestDto,
        Member member) {

        Optional<DemandForm> demandFormExist = demandFormRepository.findByProductIdAndMemberId(
            productId,
            member.getId());
        if (demandFormExist.isPresent()) {
            throw new IllegalArgumentException("이미 수요조사에 참여하였습니다.");
        }

        Product product = findProduct(productId);
        DemandForm demandForm = new DemandForm(requestDto.getQuantity(), member, product);
        demandFormRepository.save(demandForm);

        return new DemandFormResponseDto(demandForm);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
            new IllegalArgumentException("상품을 찾을 수 없습니다.")
        );
    }
}

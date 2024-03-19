package com.example.demandForm.controller;

import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.service.DemandFormService;
import com.example.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemandFormController {

    private final DemandFormService demandFormService;

    @PostMapping("/products/{productId}/demand")
    public ResponseEntity<DemandFormResponseDto> demand(
        @PathVariable Long productId,
        @Valid @RequestBody DemandFormRequestDto requestDto,
        Member member) {

        DemandFormResponseDto responseDto = demandFormService.demand(productId, requestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
//
//    @GetMapping("/mypage/demand")
//    public ResponseEntity<Page<DemandFormResponseDto>> getDemandList(
//        @RequestParam("page") int page,
//        @RequestParam("size") int size,
//        Member member) {
//
//        Page<DemandFormResponseDto> demandPage = demandFormService.getDemandList(page - 1, size,
//            member);
//
//        return ResponseEntity.status(HttpStatus.OK).body(demandPage);
//    }
}

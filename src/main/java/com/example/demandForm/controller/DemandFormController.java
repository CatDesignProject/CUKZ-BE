package com.example.demandForm.controller;

import com.example.demandForm.dto.DemandFormNonMemberRequestDto;
import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.service.DemandFormService;
import com.example.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DemandFormController {

    private final DemandFormService demandFormService;

    @PostMapping("/products/{productId}/demand/member")
    public ResponseEntity<DemandFormResponseDto> demandMember(
            @PathVariable Long productId,
            @Valid @RequestBody DemandFormRequestDto requestDto,
            @AuthenticationPrincipal Member member) {   // 로그인 구현 후 수정 예정

        DemandFormResponseDto responseDto = demandFormService.demandMember(productId, requestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/products/{productId}/demand/non-member")
    public ResponseEntity<DemandFormResponseDto> demandNonMember(
            @PathVariable Long productId,
            @Valid @RequestBody DemandFormRequestDto requestDto) {

        DemandFormResponseDto responseDto = demandFormService.demandNonMember(productId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/members/demand")
    public ResponseEntity<Page<DemandFormResponseDto>> getAllDemandFormsMember(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal Member member) {

        Page<DemandFormResponseDto> responseDtoList = demandFormService.getAllDemandFormsMember(page - 1, size, member);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/members/demand/{demandFormId}")
    public ResponseEntity<DemandFormResponseDto> getDemandFormMember(
            @PathVariable Long demandFormId) {

        DemandFormResponseDto responseDto = demandFormService.getDemandFormMember(demandFormId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/demand/non-member")
    public ResponseEntity<DemandFormResponseDto> getDemandFormNonMember(
            @RequestBody DemandFormNonMemberRequestDto requestDto) {

        DemandFormResponseDto responseDto = demandFormService.getDemandFormNonMember(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

package com.example.demandForm.controller;

import com.example.common.global.BaseResponse;
import com.example.common.global.PageResponseDto;
import com.example.demandForm.dto.request.CreateDemandFormRequestDto;
import com.example.demandForm.dto.request.DemandFormNonMemberRequestDto;
import com.example.demandForm.dto.request.UpdateDemandFormRequestDto;
import com.example.demandForm.dto.response.DemandFormResponseDto;
import com.example.demandForm.service.DemandFormService;
import com.example.security.authentication.AuthenticatedMember;
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

    // 유저
    @PostMapping("/products/{productId}/demand/members")
    public ResponseEntity<BaseResponse<DemandFormResponseDto>> demandMember(
            @PathVariable Long productId,
            @Valid @RequestBody CreateDemandFormRequestDto requestDto,
            @AuthenticationPrincipal AuthenticatedMember member) {

        DemandFormResponseDto responseDto = demandFormService.demandMember(productId, requestDto, member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, responseDto));
    }

    @PostMapping("/products/{productId}/demand/non-members")
    public ResponseEntity<BaseResponse<DemandFormResponseDto>> demandNonMember(
            @PathVariable Long productId,
            @Valid @RequestBody CreateDemandFormRequestDto requestDto) {

        DemandFormResponseDto responseDto = demandFormService.demandNonMember(productId, requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, responseDto));
    }

    @GetMapping("/members/demand")
    public ResponseEntity<BaseResponse<PageResponseDto<DemandFormResponseDto>>> getAllDemandFormsMember(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal AuthenticatedMember member) {

        Page<DemandFormResponseDto> responseDtoList = demandFormService.getAllDemandFormsMember(page - 1, size,
                member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, PageResponseDto.toResponseDto(responseDtoList)));
    }

    @GetMapping("/members/demand/{demandFormId}")
    public ResponseEntity<BaseResponse<DemandFormResponseDto>> getDemandFormMember(
            @PathVariable Long demandFormId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        DemandFormResponseDto responseDto = demandFormService.getDemandFormMember(demandFormId, member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, responseDto));
    }

    @GetMapping("/demand/non-members")
    public ResponseEntity<BaseResponse<DemandFormResponseDto>> getDemandFormNonMember(
            @RequestBody DemandFormNonMemberRequestDto requestDto) {

        DemandFormResponseDto responseDto = demandFormService.getDemandFormNonMember(requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, responseDto));
    }

    // 총대
    @GetMapping("/products/{productId}/demand")
    public ResponseEntity<BaseResponse<PageResponseDto<DemandFormResponseDto>>> getAllDemandForms(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member) {

        Page<DemandFormResponseDto> responseDtoList = demandFormService.getAllDemandForms(page - 1, size, productId,
                member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, PageResponseDto.toResponseDto(responseDtoList)));
    }

    @PatchMapping("/products/{productId}/demand/open")
    public ResponseEntity<BaseResponse<String>> startDemandForm(
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member,
            @RequestBody UpdateDemandFormRequestDto requestDto) {

        demandFormService.startDemandForm(productId, member.getMemberId(), requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, "수요조사를 시작합니다."));
    }
}

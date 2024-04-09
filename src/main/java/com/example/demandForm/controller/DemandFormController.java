package com.example.demandForm.controller;

import com.example.common.global.BaseResponse;
import com.example.common.global.PageResponseDto;
import com.example.demandForm.dto.request.DemandFormRequestDto;
import com.example.demandForm.dto.request.GetFormNonMemberRequestDto;
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
            @Valid @RequestBody DemandFormRequestDto requestDto,
            @AuthenticationPrincipal AuthenticatedMember member) {

        DemandFormResponseDto responseDto = demandFormService.demandMember(productId, requestDto, member.getMemberId());

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.CREATED, responseDto));
    }

    @PostMapping("/products/{productId}/demand/non-members")
    public ResponseEntity<BaseResponse<DemandFormResponseDto>> demandNonMember(
            @PathVariable Long productId,
            @Valid @RequestBody DemandFormRequestDto requestDto) {

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
            @RequestBody GetFormNonMemberRequestDto requestDto) {

        DemandFormResponseDto responseDto = demandFormService.getDemandFormNonMember(requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, responseDto));
    }

    // 관리자
    @DeleteMapping("/demand/{demandFormId}")
    public ResponseEntity<BaseResponse<String>> deleteDemandForm(
            @PathVariable Long demandFormId) {

        demandFormService.deleteDemandForm(demandFormId);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, "수요조사 내역이 삭제되었습니다."));
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
}

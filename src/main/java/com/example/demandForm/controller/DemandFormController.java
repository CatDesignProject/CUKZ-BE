package com.example.demandForm.controller;

import com.example.common.global.BaseResponse;
import com.example.common.global.PageResponseDto;
import com.example.demandForm.dto.DemandFormNonMemberRequestDto;
import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.dto.UpdateDemandFormRequestDto;
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
            @RequestBody DemandFormNonMemberRequestDto requestDto) {

        DemandFormResponseDto responseDto = demandFormService.getDemandFormNonMember(requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, responseDto));
    }

    // 총대
    @PatchMapping("/products/{productId}/demand/open")
    public ResponseEntity<BaseResponse<Void>> startDemandForm(
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthenticatedMember member,
            @RequestBody UpdateDemandFormRequestDto requestDto) {

        demandFormService.startDemandForm(productId, member.getMemberId(), requestDto);

        return ResponseEntity.ok().body(BaseResponse.of(HttpStatus.OK, null));
    }
}

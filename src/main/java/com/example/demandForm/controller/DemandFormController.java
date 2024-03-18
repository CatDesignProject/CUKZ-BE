package com.example.demandForm.controller;

import com.example.demandForm.dto.DemandFormRequestDto;
import com.example.demandForm.dto.DemandFormResponseDto;
import com.example.demandForm.service.DemandFormService;
import com.example.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        @AuthenticationPrincipal Member member) {

        DemandFormResponseDto responseDto = demandFormService.demand(productId, requestDto, member);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

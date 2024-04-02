package com.example.demandForm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDemandFormRequestDto {

    LocalDateTime startDate;
    LocalDateTime endDate;
}

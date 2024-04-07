package com.example.demandForm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDemandFormRequestDto {

    private String email;

    private List<FormOptionRequestDto> optionList;
}
package com.example.demandForm.controller;

import com.example.demandForm.service.demandFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class demandFormController {

    private final demandFormService demandFormService;
}

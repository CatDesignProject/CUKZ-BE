package com.example.demandForm.service;

import com.example.demandForm.repository.demandFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class demandFormService {

    private final demandFormRepository demandFormRepository;

    
}

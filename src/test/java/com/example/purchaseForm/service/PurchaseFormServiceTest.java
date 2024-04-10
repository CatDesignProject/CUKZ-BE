package com.example.purchaseForm.service;

import com.example.demandForm.DemandTestBuilder;
import com.example.product.ProductTestBuilder;
import com.example.product.entity.Option;
import com.example.product.entity.Product;
import com.example.product.repository.OptionRepository;
import com.example.product.repository.ProductRepository;
import com.example.purchaseForm.PurchaseTestBuilder;
import com.example.purchaseForm.dto.PurchaseFormRequestDto;
import com.example.purchaseForm.entity.PurchaseForm;
import com.example.purchaseForm.entity.PurchaseOption;
import com.example.purchaseForm.repository.PurchaseFormRepository;
import com.example.purchaseForm.repository.PurchaseOptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PurchaseFormServiceTest {

    @Mock
    PurchaseFormRepository purchaseFormRepository;
    @Mock
    PurchaseOptionRepository purchaseOptionRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    OptionRepository optionRepository;
    @InjectMocks
    PurchaseFormService purchaseFormService;

    Product product;
    PurchaseForm purchaseForm;
    Option option;
    PurchaseOption purchaseOption;
    PurchaseFormRequestDto requestDto;
    Long productId = 1L;

    @BeforeEach
    void setUp() {
        product = ProductTestBuilder.testProductBuild();
        ReflectionTestUtils.setField(product, "id", productId);

        option = DemandTestBuilder.buildOption();
        purchaseOption = PurchaseTestBuilder.buildPurchaseOption();
        purchaseForm = PurchaseTestBuilder.buildPurchaseForm();
        requestDto = PurchaseTestBuilder.buildPurchaseFormRequestDto();
    }
}
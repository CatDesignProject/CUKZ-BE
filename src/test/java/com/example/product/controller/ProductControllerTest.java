package com.example.product.controller;

import com.example.product.dto.request.ProductRequestDto;
import com.example.product.enums.ProductType;
import com.example.product.enums.SaleStatus;
import com.example.product.enums.Size;
import com.example.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @DisplayName("상품 등록 테스트")
    @Test
    void save_Product() throws Exception {
        //given
        List<String> colors = new ArrayList<>();
        colors.add("NAVY");

        List<Size> sizes = new ArrayList<>();
        sizes.add(Size.S);
        sizes.add(Size.L);

        List<Long> productImageIds = new ArrayList<>();
        productImageIds.add(1L);
        productImageIds.add(2L);

        LocalDateTime startDate = LocalDateTime.of(2024, 3, 18, 15, 30, 0);

        // 지정된 연, 월, 일, 시, 분, 초를 사용하여 LocalDateTime 객체 생성
        LocalDateTime endDate = LocalDateTime.of(2024, 3, 19, 15, 30, 0);

        ProductRequestDto productRequestDto = new ProductRequestDto(ProductType.잠바, SaleStatus.ON_SALE
                , "컴공 19 과잠", 30000
                , "가톨릭대 컴공 19 과잠입니다."
                , startDate
                , endDate
                , colors
                , sizes
                , productImageIds
        );

        String content = objectMapper.writeValueAsString(productRequestDto);

        given(productService.saveProduct(productRequestDto)).willReturn(1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andDo(print());

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("상품 ID : 1번이 등록 되었습니다."))
                .andReturn();

        Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("상품 ID : 1번이 등록 되었습니다.");
    }
}
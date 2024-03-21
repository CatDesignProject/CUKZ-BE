package com.example.product_image.controller;

import com.example.product_image.dto.response.ProductImageResponse;
import com.example.product_image.service.ProductImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductImageController.class)
class ProductImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductImageService productImageService;

    public static class CustomMultipartFile {

        static final String IMAGE_URL =
                "https://jsg6854bucket.s3.ap-northeast-2.amazonaws.com/3a44b93e-976a-4f78-a7bd-e16aceecd110.png";
        static final String IMAGE_URL2 =
                "https://jsg6854bucket.s3.ap-northeast-2.amazonaws.com/2f2773f8-7853-4561-891f-cac45ba894b4.png";
    }

    @DisplayName("이미지 업로드")
    @Test
    void uploadFile() throws Exception {
        //given
        List<MultipartFile> files = new ArrayList<>();

        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "example1.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "example2.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!2".getBytes()
        );

        List<ProductImageResponse> productImageResponses = new ArrayList<>();
        ProductImageResponse productImageResponse = new ProductImageResponse(CustomMultipartFile.IMAGE_URL, 1L);
        ProductImageResponse productImageResponse2 = new ProductImageResponse(CustomMultipartFile.IMAGE_URL2, 2L);
        productImageResponses.add(productImageResponse);
        productImageResponses.add(productImageResponse2);

        given(productImageService.fileUpload(
                any())).willReturn(productImageResponses);

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/products/image-upload")
                        .file(file1)
                        .file(file2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andReturn();
    }
}
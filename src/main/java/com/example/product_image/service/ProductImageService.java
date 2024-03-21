package com.example.product_image.service;

import com.example.product_image.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    List<ProductImageResponse> fileUpload(List<MultipartFile> file);
    String getPath(String imagePath);
}

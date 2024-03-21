package com.example.product.service;

import com.example.product.dto.request.ProductRequestDto;

public interface ProductService {

    Long saveProduct(ProductRequestDto productRequestDto);

}

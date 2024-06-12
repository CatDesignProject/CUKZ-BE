package com.example.common.scheduler;

import com.example.product.entity.Product;
import com.example.product.enums.SaleStatus;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusScheduler {

    private final ProductRepository productRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateSaleStatus() {

        log.info("Update Product SaleStatus");

        LocalDate today = LocalDate.now();

        List<Product> demandProductList = productRepository.findEndedProduct(today, SaleStatus.ON_DEMAND);
        demandProductList.forEach(product -> product.updateStatus(SaleStatus.END_DEMAND));

        List<Product> purchaseProductList = productRepository.findEndedProduct(today, SaleStatus.ON_SALE);
        purchaseProductList.forEach(product -> product.updateStatus(SaleStatus.END_SALE));
    }
}

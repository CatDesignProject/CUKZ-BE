package com.example.purchaseForm;

import java.time.LocalDate;

public interface PurchaseTest {
    String BUYER_NAME = "구매자 이름";
    String BUYER_PHONE = "010-1234-5678";
    String BUYER_EMAIL = "byuer@email.com";
    String RECIPIENT_NAME = "수취인 이름";
    String RECIPIENT_PHONE = "010-1234-5678";
    String ADDRESS = "서울시 강남구";
    String PAYER_NAME = "결제자 이름";
    LocalDate PAY_DATE = LocalDate.now();
    String REFUND_NAME = "환불자 이름";
    String REFUND_ACCOUNT = "환불 계좌";
    int ADDITIONAL_PRICE = 1000;
}

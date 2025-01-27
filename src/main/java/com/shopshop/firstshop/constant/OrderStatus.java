package com.shopshop.firstshop.constant;

public enum OrderStatus {
    ORDER_RECEIVED,    // 주문 접수
    PAYMENT_COMPLETED, // 결제 완료
    PREPARING,        // 상품 준비중
    SHIPPING,         // 배송중
    DELIVERED,        // 배송 완료
    CANCELED          // 주문 취소
} 
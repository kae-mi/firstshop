package com.shopshop.firstshop.controller;

import com.shopshop.firstshop.dto.OrderDto;
import com.shopshop.firstshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody List<OrderDto> orderList) {
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        log.info("사용자 이메일: {}", email);
        log.info("주문 요청 데이터: {}", orderList);

        // 주문 처리 로직 실행
        try {
            Long orderId = orderService.order(email, orderList);
            return ResponseEntity.ok().body(Map.of("success", true, "orderId", orderId));
        } catch (Exception e) {
            log.error("주문 처리 중 오류 발생", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "errorMessage", "주문 처리 중 오류가 발생했습니다."));
        }
    }


    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
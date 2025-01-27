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

@Controller
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    public String createOrder(@ModelAttribute OrderDto orderDto, Model model) {
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("auth: {}", auth);

        String email = auth.getName();
        log.info("email: {}", email);

        try {
            Long orderId = orderService.order(email, orderDto);
            model.addAttribute("orderId", orderId);
            return "redirect:/"; // 주문 상세 페이지로 리다이렉트
        } catch (Exception e) {
            model.addAttribute("errorMessage", "주문 처리 중 오류가 발생했습니다.");
            return "error/order"; // 에러 페이지로 이동
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
} 
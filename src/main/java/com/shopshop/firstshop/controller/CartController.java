package com.shopshop.firstshop.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.shopshop.firstshop.dto.OrderDto;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopshop.firstshop.dto.CartDetailDto;
import com.shopshop.firstshop.dto.CartItemDto;
import com.shopshop.firstshop.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    // 장바구니 상품 추가
    @PostMapping
    @ResponseBody
    public ResponseEntity<String> addCart(@RequestBody CartItemDto cartItemDto,
                                        Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            String email = principal.getName();
            cartService.addCart(cartItemDto, email);
            return new ResponseEntity<>("상품을 장바구니에 담았습니다.", HttpStatus.OK);
        } catch (Exception e) {
            getExceptionMessage(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 장바구니 목록 조회
    @GetMapping
    public String cartList(Principal principal, Model model) {

        // Principal 객체를 통해 현재 로그인한 사용자의 정보를 가져옴
        if (principal == null) {
            return "redirect:/members/login";
        }
        
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        int totalPrice = cartDetailList.stream()
        .mapToInt(CartDetailDto -> CartDetailDto.getPrice() * CartDetailDto.getCount())
        .sum();
        model.addAttribute("cartItems", cartDetailList);
        model.addAttribute("totalPrice", totalPrice); // 미리 계산된 합계 추가

        return "cart/cartList";
    }

    // 장바구니 상품 수량 변경
    @PutMapping(value = "/items/{cartItemId}/quantity")
    public ResponseEntity<Map<String, Boolean>> updateQuantity(
                @PathVariable("cartItemId") Long cartItemId,
                @RequestParam("change") int change) {
        
        try {
            cartService.updateQuantity(cartItemId, change);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            getExceptionMessage(e);
            return ResponseEntity.ok(Collections.singletonMap("success", false));
        }
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Map<String, Boolean>> removeItem(
            @PathVariable("cartItemId") Long cartItemId) {
        try {
            cartService.removeCartItem(cartItemId);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            getExceptionMessage(e);
            return ResponseEntity.ok(Collections.singletonMap("success", false));
        }
    }

    private static void getExceptionMessage(Exception e) {
        log.info("error message: {}", e.getMessage());
    }
}

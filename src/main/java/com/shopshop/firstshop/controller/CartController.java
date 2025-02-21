package com.shopshop.firstshop.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopshop.firstshop.dto.CartDetailDto;
import com.shopshop.firstshop.dto.CartItemDto;
import com.shopshop.firstshop.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    @ResponseBody
    public ResponseEntity<String> addCart(@RequestBody CartItemDto cartItemDto,
                                        Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            String email = principal.getName();
            Long cartItemId = cartService.addCart(cartItemDto, email);
            return new ResponseEntity<>("상품을 장바구니에 담았습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/cart")
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

}

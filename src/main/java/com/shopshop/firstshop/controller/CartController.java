package com.shopshop.firstshop.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopshop.firstshop.dto.CartItemDto;
import com.shopshop.firstshop.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    public ResponseEntity<String> addCart(@RequestBody @Validated CartItemDto cartItemDto,
                                            BindingResult bindingResult,
                                            Principal principal) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("장바구니 담기 실패", HttpStatus.BAD_REQUEST);
        }
        
        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<>("장바구니 담기 실패" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>("장바구니에 상품을 담았습니다.", HttpStatus.OK);
    }

}

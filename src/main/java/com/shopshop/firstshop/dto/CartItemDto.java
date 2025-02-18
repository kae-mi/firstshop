package com.shopshop.firstshop.dto;

import lombok.Getter;
import lombok.Setter;

/* 
 * 폼으로부터 받은 데이터를 엔티티로 변환하는 역할
 * 장바구니 페이지에서 장바구니 상품 추가 시 사용함
 */
@Getter @Setter
public class CartItemDto {

    private Long itemId; 

    private int count;
}

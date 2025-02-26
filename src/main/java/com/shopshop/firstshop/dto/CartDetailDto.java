package com.shopshop.firstshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDto {
    
    private Long cartItemId;
    private String itemName;
    private Long itemId;
    private int price;
    private int count;

    // Hibernate의 JPQL DTO Projection을 위한 생성자 추가
    public CartDetailDto(Long id, String itemName, int price, int count) {
        this.cartItemId = id;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
    }
}
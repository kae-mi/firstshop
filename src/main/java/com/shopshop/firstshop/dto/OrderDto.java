package com.shopshop.firstshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDto {

    private Long itemId;
    
    private int count;

    private String orderStatus;
} 
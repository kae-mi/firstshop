package com.shopshop.firstshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // 외래키 설정
    private Order order; // 주문 엔티티와 연관관계 매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 외래키 설정
    private Item item; // 상품 엔티티와 연관관계 매핑

    private int orderPrice; // 주문 가격
    
    private int count; // 주문 수량

    // 주문 상품 생성 메소드
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        
        return orderItem;
    }
} 
package com.shopshop.firstshop.entity;

import com.shopshop.firstshop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order는 예약어이므로 orders로 테이블명 지정
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래키 설정
    private Member member; // 회원은 여러 주문을 할 수 있음

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문 상품 엔티티와 연관관계 매핑

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    private String deliveryAddress; // 배송 주소    
    
    private int totalAmount; // 총 주문 금액

    //  Order와 OrderItem 간의 관계를 관리하는 메소드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 주문 생성 메소드
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);
        
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            // 주문 시점에 재고 감소
            orderItem.getItem().removeStock(orderItem.getCount());
        }

        order.setOrderStatus(OrderStatus.ORDER_RECEIVED);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryAddress(member.getAddress());
        // 총 주문 금액 계산
        
        int totalAmount = orderItems.stream()
            .mapToInt(item -> item.getOrderPrice() * item.getCount())
            .sum();
        order.setTotalAmount(totalAmount);
        
        return order;
    }

    // 주문 취소 시 재고 원복
    public void cancelOrder() {
        if(orderStatus == OrderStatus.SHIPPING || orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송중이거나 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setOrderStatus(OrderStatus.CANCELED);
        
        // 재고 원복
        for(OrderItem orderItem : orderItems) {
            orderItem.getItem().addStock(orderItem.getCount());
        }
    }
} 
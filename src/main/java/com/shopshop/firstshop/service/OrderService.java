package com.shopshop.firstshop.service;

import com.shopshop.firstshop.dto.OrderDto;
import com.shopshop.firstshop.entity.*;
import com.shopshop.firstshop.repository.CartItemRepository;
import com.shopshop.firstshop.repository.ItemRepository;
import com.shopshop.firstshop.repository.MemberRepository;
import com.shopshop.firstshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    // 주문하기 서비스
    public Long order(String name, List<OrderDto> orderList) {

        // 회원 정보 조회
        Member member = memberRepository.findByUsername(name);
        if (member == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        // 주문 상품 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderDto orderDto : orderList) {
            // 상품 조회
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + orderDto.getItemId()));

            // 주문 상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItems.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItems);

        orderRepository.save(order);

        // ✅ 주문 완료 후 장바구니에서 주문한 상품 삭제
        cartItemRepository.deleteByUsername(name);
        return order.getId();

    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        order.cancelOrder();
    }
} 